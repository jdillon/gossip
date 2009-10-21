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

import org.sonatype.gossip.listener.ConsoleListener;
import org.sonatype.gossip.model.ListenerNode;
import org.sonatype.gossip.model.Model;
import org.sonatype.gossip.model.ProfileNode;
import org.sonatype.gossip.model.SourceNode;
import org.sonatype.gossip.model.TriggerNode;
import org.sonatype.gossip.model.merge.ModelMerger;
import org.sonatype.gossip.source.Source;
import org.sonatype.gossip.source.URLSource;
import org.sonatype.gossip.trigger.AlwaysTrigger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures Gossip.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class Configurator
{
    private static final String BOOTSTRAP_RESOURCE = "bootstrap.properties";

    private final Log log = Log.getLogger(getClass());

    public EffectiveProfile configure() {
        log.debug("Configuring");

        EffectiveProfile profile = new EffectiveProfile();

        try {
            // Load the bootstrap configuration
            Model bootstrap = loadBootstrap();

            // Resolve sources and merge
            Model config = resolve(bootstrap);

            // Configure the active profiles
            configureActiveProfiles(profile, config);
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

    private Model resolve(final Model bootstrap) {
        assert bootstrap != null;

        Model config = new Model();
        ModelMerger merger = new ModelMerger();
        Map<Object,Object> hints = new HashMap<Object,Object>();

        for (SourceNode source : bootstrap.getSources()) {
            try {
                Source loader = source.create();
                merger.merge(config, loader.load(), true, hints);
            }
            catch (Exception e) {
                log.error("Failed to resolve source: " + source, e);
            }
        }

        return config;
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

        log.trace("Checking for active triggers");

        for (TriggerNode trigger : profile.getTriggers()) {
            try {
                if (trigger.create().isActive()) {
                    log.debug("Active trigger: {}", trigger);
                    return true;
                }
            }
            catch (Exception e) {
                log.error("Failed to evaluate trigger: " + trigger, e);
            }

        }

        return false;
    }

    private ProfileNode createFallbackProfile() {
        ProfileNode p = new ProfileNode();
        p.setName("fall-back");

        TriggerNode t = new TriggerNode();
        t.setType(AlwaysTrigger.class);
        p.addTrigger(t);

        ListenerNode l = new ListenerNode();
        l.setType(ConsoleListener.class);

        return p;
    }

    private Model loadBootstrap() throws Exception {
        URL url = getClass().getResource(BOOTSTRAP_RESOURCE);

        // This should really never happen unless something is messed up, but don't toss an exception, let the fallback provider kickin
        assert url != null : "Unable to load bootstrap resource: " + BOOTSTRAP_RESOURCE;

        log.trace("Using bootstrap URL: {}", url);
        
        URLSource source = new URLSource();
        source.setUrl(url);
        
        return source.load();
    }
}