/*
 * Copyright (c) 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonatype.gossip;

import org.slf4j.Logger;
import org.sonatype.gossip.listener.ConsoleListener;
import org.sonatype.gossip.model.ListenerNode;
import org.sonatype.gossip.model.LoggerNode;
import org.sonatype.gossip.model.Model;
import org.sonatype.gossip.model.ModelMerger;
import org.sonatype.gossip.model.ProfileNode;
import org.sonatype.gossip.model.SourceNode;
import org.sonatype.gossip.model.TriggerNode;
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
 * @since 1.0
 */
public final class Configurator
{
    private static final String BOOTSTRAP_RESOURCE = "bootstrap.properties";

    private final Logger log = Log.getLogger(getClass());

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

        if (profile.getProfiles().isEmpty()) {
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

        resolveIncludes(config);

        return config;
    }

    private void resolveIncludes(final Model model) {
        assert model != null;

        // Process profile includes
        for (ProfileNode profile : model.getProfiles()) {
            log.trace("Processing includes for: {}", profile);

            for (String include : profile.getIncludes()) {
                ProfileNode includedProfile = model.findProfile(include);

                if (includedProfile == null) {
                    log.warn("Unable to include non-existent profile: {}", includedProfile);
                    continue;
                }

                log.debug("Including {} profile into: {}", include, profile);

                // FIXME: This should really be in ModelMerger

                for (Object name : includedProfile.getProperties().keySet()) {
                    if (!profile.getProperties().containsKey(name)) {
                        profile.getProperties().put(name, includedProfile.getProperties().get(name));
                        log.trace("Appending property: {}", name);
                    }
                }

                for (LoggerNode logger : includedProfile.getLoggers()) {
                    if (!profile.getLoggers().contains(logger)) {
                        profile.getLoggers().add(logger);
                        log.trace("Appending logger: {}", logger);
                    }
                }

                for (ListenerNode listener : includedProfile.getListeners()) {
                    if (!profile.getListeners().contains(listener)) {
                        profile.getListeners().add(listener);
                        log.trace("Appending listener: {}", listener);
                    }
                }

                for (TriggerNode trigger : includedProfile.getTriggers()) {
                    if (!profile.getTriggers().contains(trigger)) {
                        profile.getTriggers().add(trigger);
                        log.trace("Appending trigger: {}", trigger);
                    }
                }
            }
        }
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

        // If no profiles were activated, look for a "default" profile, if it exists then use it
        if (profile.getProfiles().isEmpty()) {
            ProfileNode node = model.findProfile("default");
            if (node != null) {
                log.debug("Using default profile: {}", node);
                profile.addProfile(node);
            }
        }
    }

    private boolean isProfileActive(final ProfileNode profile) {
        assert profile != null;

        log.trace("Checking if profile is active: {}", profile);

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

        TriggerNode trigger = new TriggerNode();
        trigger.setType(AlwaysTrigger.class);
        p.getTriggers().add(trigger);

        ListenerNode listenerNode = new ListenerNode();
        listenerNode.setType(ConsoleListener.class);
        p.getListeners().add(listenerNode);

        return p;
    }

    private Model loadBootstrap() throws Exception {
        URL url = getClass().getResource(BOOTSTRAP_RESOURCE);

        // This should really never happen unless something is messed up, but don't toss an exception, let the fallback provider kick-in
        assert url != null : "Unable to load bootstrap resource: " + BOOTSTRAP_RESOURCE;

        log.trace("Using bootstrap URL: {}", url);
        
        URLSource source = new URLSource();
        source.setUrl(url);
        
        return source.load();
    }
}