/*
 * Copyright (c) 2009-present the original author or authors.
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
package com.planet57.gossip.model.io.props;

import com.planet57.gossip.ConfigurationException;
import com.planet57.gossip.model.ListenerNode;
import com.planet57.gossip.model.LoggerNode;
import com.planet57.gossip.model.Model;
import com.planet57.gossip.model.SourceNode;
import com.planet57.gossip.model.TriggerNode;
import org.slf4j.Logger;
import com.planet57.gossip.Log;
import com.planet57.gossip.model.ProfileNode;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Reads a Gossip {@link Model} from a properties file.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class GossipPropertiesReader
{
    private static final String EXPECTED_VERSION = "1.0.0";

    private static final String SOURCES = "sources";

    private static final String SOURCE_DOT = "source.";

    private static final String PROFILES = "profiles";

    private static final String PROFILE_DOT = "profile.";

    private static final String INCLUDES = "includes";

    private static final String PROPERTIES = "properties";

    private static final String LOGGER = "logger";

    private static final String TRIGGERS = "triggers";

    private static final String TRIGGER_DOT = "trigger.";

    private static final String LISTENERS = "listeners";

    private static final String LISTENER_DOT = "listener.";

    private static final Logger log = Log.getLogger(GossipPropertiesReader.class);

    //
    // TODO: Support XML properties format
    //
    
    public Model read(final URL url) throws IOException {
        assert url != null;

        Model model = new Model();

        Context ctx = Context.create(url);

        // Validate the version
        String tmp = ctx.get("version");
        if (!EXPECTED_VERSION.equals(tmp)) {
            throw new ConfigurationException("Invalid configuration version: " + tmp + ", expected: " + EXPECTED_VERSION);
        }

        model.setProperties(ctx.child(PROPERTIES).toProperties());

        configureSourceNodes(model, ctx);

        configureProfileNodes(model, ctx);

        return model;
    }

    private void configureSourceNodes(final Model model, final Context ctx) {
        assert model != null;
        assert ctx != null;

        if (!ctx.contains(SOURCES)) {
            return;
        }

        for (String name : ctx.split(SOURCES, true)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Source name is blank");
            }

            log.trace("Configuring source: {}", name);

            SourceNode node = createSourceNode(name, ctx.get(SOURCE_DOT + name), ctx.child(SOURCE_DOT + name));
            model.getSources().add(node);
        }
    }

    private SourceNode createSourceNode(final String id, final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring source: {} -> {}", type, ctx);

        SourceNode node = new SourceNode();
        node.setId(id);
        node.setType(type);
        node.setConfiguration(ctx);

        log.trace("Created: {}", node);

        return node;
    }

    private void configureProfileNodes(final Model model, final Context ctx) {
        assert model != null;
        assert ctx != null;

        if (!ctx.contains(PROFILES)) {
            return;
        }

        for (String name : ctx.split(PROFILES, true)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Profile name is blank");
            }

            ProfileNode node = createProfileNode(name, ctx.child(PROFILE_DOT + name));
            model.getProfiles().add(node);
        }
    }

    private ProfileNode createProfileNode(final String name, final Context ctx) {
        assert name != null;
        assert ctx != null;

        log.trace("Configuring profile: {} -> {}", name, ctx);

        ProfileNode node = new ProfileNode();
        node.setId(name);
        node.setName(name);
        node.setProperties(ctx.child(PROPERTIES).toProperties());

        configureLoggerNodes(node, ctx.child(LOGGER));
        configureTriggerNodes(node, ctx);
        configureListenerNodes(node, ctx);

        String includes = ctx.get(INCLUDES);
        if (includes != null) {
            String[] profiles = Context.trim(includes.split(","));
            node.setIncludes(Arrays.asList(profiles));
        }

        log.trace("Created: {}", node);

        return node;
    }

    private void configureLoggerNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        for (String name : ctx.names()) {
            name = name.trim();
            String value = ctx.get(name);

            LoggerNode node = new LoggerNode();
            node.setId(name);
            node.setName(name);
            node.setLevel(value);

            log.trace("Created: {}", node);

            profile.getLoggers().add(node);
        }
    }

    private void configureTriggerNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        if (!ctx.contains(TRIGGERS)) {
            return;
        }

        for (String name : ctx.split(TRIGGERS, true)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Trigger name is blank");
            }

            TriggerNode node = createTriggerNode(name, ctx.get(TRIGGER_DOT + name), ctx.child(TRIGGER_DOT + name));
            profile.getTriggers().add(node);
        }
    }

    private TriggerNode createTriggerNode(final String id, final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring trigger: {} -> {}", type, ctx);

        TriggerNode node = new TriggerNode();
        node.setId(id);
        node.setType(type);
        node.setConfiguration(ctx);

        log.trace("Created: {}", node);

        return node;
    }

    private void configureListenerNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        if (!ctx.contains(LISTENERS)) {
            return;
        }

        for (String name : ctx.split(LISTENERS, true)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Listener name is blank");
            }

            ListenerNode listener = createListenerNode(name, ctx.get(LISTENER_DOT + name), ctx.child(LISTENER_DOT + name));
            profile.getListeners().add(listener);
        }
    }

    private ListenerNode createListenerNode(final String id, final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring listener: {} -> {}", type, ctx);

        ListenerNode node = new ListenerNode();
        node.setId(id);
        node.setType(type);
        node.setConfiguration(ctx);

        log.trace("Created: {}", node);

        return node;
    }
}