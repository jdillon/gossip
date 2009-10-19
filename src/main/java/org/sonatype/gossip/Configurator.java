/*
 * Copyright (C) 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.gossip;

import org.sonatype.gossip.Log;
import org.sonatype.gossip.EffectiveProfile;
import org.sonatype.gossip.filter.ConsoleWritingFilter;
import org.sonatype.gossip.model2.FilterNode;
import org.sonatype.gossip.model2.Model;
import org.sonatype.gossip.model2.ProfileNode;
import org.sonatype.gossip.model2.TriggerNode;
import org.sonatype.gossip.source.URLSource;
import org.sonatype.gossip.trigger.AlwaysTrigger;

import java.net.URL;

/**
 * Configures Gossip.
 *
 * @since 1.0
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class Configurator
{
    private static final String BOOTSTRAP_RESOURCE = "bootstrap.properties";

    private final Log log = Log.getLogger(getClass());

    public EffectiveProfile configure() {
        log.debug("Configuring");

        Model root = new Model();

        EffectiveProfile profile = new EffectiveProfile();

        try {
            // FIXME:
            
            // Load the bootstrap configuration
            // Model bootstrap = loadBootstrap();

            // Resolve sources and merge
            // Model config = resolve(bootstrap, root);

            // Configure the active profiles
            // configureActiveProfiles(profile, config);
        }
        catch (Throwable t) {
            log.error("Failed to configure; using fall-back provider", t);
        }

        if (profile.profiles().isEmpty()) {
            log.debug("No profiles were activated; using fall-back");

            ProfileNode p = createFallbackProfile();
            profile.addProfile(p);
        }

        return profile;
    }

    private void configureActiveProfiles(final EffectiveProfile profile, final Model model) throws Exception {
        assert profile != null;
        assert model != null;

        log.debug("Activating profiles");

        for (ProfileNode node : model.getProfiles()) {
            if (isProfileActive(node)) {
                log.debug("Active profile: {}", node);
                profile.addProfile(node);
            }
        }
    }

    private boolean isProfileActive(final ProfileNode profile) {
        assert profile != null;

        // FIXME: Check if profile is active

        /*
        // No triggers means its not active
        if (triggers == null) {
            log.trace("No triggers found; profile is not active");

            return false;
        }

        log.trace("Checking for active triggers");

        for (Trigger trigger : triggers()) {
            // If active, then stop now
            if (trigger.isActive()) {
                log.debug("Active trigger: {}", trigger);
                return true;
            }
        }

        return false;
         */

        return false;
    }

    private ProfileNode createFallbackProfile() {
        ProfileNode p = new ProfileNode();
        p.setName("fallback");

        TriggerNode t = new TriggerNode();
        t.setType(AlwaysTrigger.class.getName());
        p.addTrigger(t);

        FilterNode f = new FilterNode();
        f.setType(ConsoleWritingFilter.class.getName());

        return p;
    }

    private Model loadBootstrap() throws Exception {
        URL url = getClass().getResource(BOOTSTRAP_RESOURCE);

        // This should really never happen unless something is messed up, but don't toss an exception, let the fallback provider kickin
        assert url != null : "Unable to load bootstrap resource: " + BOOTSTRAP_RESOURCE;

        log.trace("Using bootstrap URL: {}", url);
        
        URLSource source = new URLSource(url);

        return source.load();
    }
}