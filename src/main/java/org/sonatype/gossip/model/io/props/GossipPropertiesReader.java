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

package org.sonatype.gossip.model.io.props;

import org.sonatype.gossip.ConfigurationException;
import org.sonatype.gossip.Log;
import org.sonatype.gossip.model.ListenerNode;
import org.sonatype.gossip.model.LoggerNode;
import org.sonatype.gossip.model.Model;
import org.sonatype.gossip.model.ProfileNode;
import org.sonatype.gossip.model.SourceNode;
import org.sonatype.gossip.model.TriggerNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Reads a Gossip {@link Model} from a properties file.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class GossipPropertiesReader
{
    private static final String EXPECTED_VERSION = "1.0.0";

    private static final String SOURCES = "sources";

    private static final String SOURCE_DOT = "source.";

    private static final String PROFILES = "profiles";

    private static final String PROFILE_DOT = "profile.";

    private static final String PROPERTIES = "properties";

    private static final String LOGGER = "logger";

    private static final String TRIGGERS = "triggers";

    private static final String TRIGGER_DOT = "trigger.";

    private static final String LISTENERS = "listeners";

    private static final String LISTENER_DOT = "listener.";

    private static final Log log = Log.getLogger(GossipPropertiesReader.class);

    //
    // TODO: Support XML properties format
    //
    
    public Model read(final InputStream in) throws IOException {
        assert in != null;

        Model model = new Model();

        Context ctx = Context.create(in);

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

    public Model read(final URL url) throws IOException {
        InputStream in = url.openStream();

        try {
            return read(in);
        }
        finally {
            in.close();
        }
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

            SourceNode source = createSourceNode(ctx.get(SOURCE_DOT + name), ctx.child(SOURCE_DOT + name));
            model.addSource(source);
        }
    }

    private SourceNode createSourceNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring source: {} -> {}", type, ctx);

        SourceNode source = new SourceNode();
        source.setType(type);
        source.setConfiguration(ctx);

        log.trace("Created: {}", source);

        return source;
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

            ProfileNode profile = createProfileNode(name, ctx.child(PROFILE_DOT + name));
            model.addProfile(profile);
        }
    }

    private ProfileNode createProfileNode(final String name, final Context ctx) {
        assert name != null;
        assert ctx != null;

        log.trace("Configuring profile: {} -> {}", name, ctx);

        ProfileNode profile = new ProfileNode();
        profile.setName(name);
        profile.setProperties(ctx.child(PROPERTIES).toProperties());

        configureLoggerNodes(profile, ctx.child(LOGGER));
        configureTriggerNodes(profile, ctx);
        configureListenerNodes(profile, ctx);

        log.trace("Created: {}", profile);

        return profile;
    }

    private void configureLoggerNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        for (String name : ctx.names()) {
            name = name.trim();
            String value = ctx.get(name);

            LoggerNode logger = new LoggerNode();
            logger.setName(name);
            logger.setLevel(value);

            log.trace("Created: {}", logger);

            profile.addLogger(logger);
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

            TriggerNode trigger = createTriggerNode(ctx.get(TRIGGER_DOT + name), ctx.child(TRIGGER_DOT + name));
            profile.addTrigger(trigger);
        }
    }

    private TriggerNode createTriggerNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring trigger: {} -> {}", type, ctx);

        TriggerNode trigger = new TriggerNode();
        trigger.setType(type);
        trigger.setConfiguration(ctx);

        log.trace("Created: {}", trigger);

        return trigger;
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

            ListenerNode listener = createListenerNode(ctx.get(LISTENER_DOT + name), ctx.child(LISTENER_DOT + name));
            profile.addListener(listener);
        }
    }

    private ListenerNode createListenerNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Configuring listener: {} -> {}", type, ctx);

        ListenerNode listener = new ListenerNode();
        listener.setType(type);
        listener.setConfiguration(ctx);

        log.trace("Created: {}", listener);

        return listener;
    }
}