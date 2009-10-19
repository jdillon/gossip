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

package org.sonatype.gossip.model2.io.props;

import org.sonatype.gossip.Log;
import org.sonatype.gossip.config.ConfigurationException;
import org.sonatype.gossip.model2.FilterNode;
import org.sonatype.gossip.model2.LoggerNode;
import org.sonatype.gossip.model2.Model;
import org.sonatype.gossip.model2.ProfileNode;
import org.sonatype.gossip.model2.SourceNode;
import org.sonatype.gossip.model2.TriggerNode;

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

    private static final String FILTERS = "filters";

    private static final String FILTER_DOT = "filter.";

    private static final Log log = Log.getLogger(GossipPropertiesReader.class);

    public Model read(final InputStream in) throws IOException {
        assert in != null;

        log.trace("Creating model from: {}", in);

        Model model = new Model();

        Context ctx = Context.create(in);

        // Validate the version
        String tmp = ctx.get("version", (String)null);
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

        log.trace("Configuring sources: {}", ctx);

        if (!ctx.contains(SOURCES)) {
            log.trace("Missing 'sources' property; skipping");
            return;
        }

        for (String name : ctx.split(SOURCES)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Source name must not be blank");
            }

            SourceNode source = createSourceNode(ctx.get(SOURCE_DOT + name, (String)null), ctx.child(SOURCE_DOT + name));
            model.addSource(source);
        }
    }

    private SourceNode createSourceNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Creating source: {}", type);

        SourceNode source = new SourceNode();
        source.setType(type);
        source.setConfiguration(ctx);

        log.trace("Created: {}", source);

        return source;
    }

    private void configureProfileNodes(final Model model, final Context ctx) {
        assert model != null;
        assert ctx != null;

        log.trace("Configuring profiles: {}", ctx);

        if (!ctx.contains(PROFILES)) {
            log.trace("Missing 'profiles' property; skipping");
            return;
        }

        for (String name : ctx.split(PROFILES)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Profile name must not be blank");
            }

            ProfileNode profile = createProfileNode(name, ctx.child(PROFILE_DOT + name));
            model.addProfile(profile);
        }
    }

    private ProfileNode createProfileNode(final String name, final Context ctx) {
        assert name != null;
        assert ctx != null;

        log.trace("Creating profile: {}", name);

        ProfileNode profile = new ProfileNode();
        profile.setName(name);
        profile.setProperties(ctx.child(PROPERTIES).toProperties());

        configureLoggerNodes(profile, ctx.child(LOGGER));

        configureTriggerNodes(profile, ctx);

        configureFilterNodes(profile, ctx);

        log.trace("Created: {}", profile);

        return profile;
    }

    private void configureLoggerNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        log.trace("Configuring loggers: {}", ctx);

        for (String name : ctx.names()) {
            String value = ctx.get(name, (String)null);

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

        log.trace("Configuring triggers: {}", ctx);

        if (!ctx.contains(TRIGGERS)) {
            log.trace("Missing 'triggers' property; skipping");
            return;
        }

        for (String name : ctx.split(TRIGGERS)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Trigger name must not be blank");
            }

            TriggerNode trigger = createTriggerNode(ctx.get(TRIGGER_DOT + name, (String)null), ctx.child(TRIGGER_DOT + name));
            profile.addTrigger(trigger);
        }
    }

    private TriggerNode createTriggerNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Creating trigger: {}", type);

        TriggerNode trigger = new TriggerNode();
        trigger.setType(type);
        trigger.setConfiguration(ctx);

        log.trace("Created: {}", trigger);

        return trigger;
    }

    private void configureFilterNodes(final ProfileNode profile, final Context ctx) {
        assert profile != null;
        assert ctx != null;

        log.trace("Configuring filters: {}", ctx);

        if (!ctx.contains(FILTERS)) {
            log.trace("Missing 'filters' property; skipping");
            return;
        }

        for (String name : ctx.split(FILTERS)) {
            if (name.length() == 0) {
                throw new ConfigurationException("Filter name must not be blank");
            }

            FilterNode filter = createFilterNode(ctx.get(FILTER_DOT + name, (String)null), ctx.child(FILTER_DOT + name));
            profile.addFilter(filter);
        }
    }

    private FilterNode createFilterNode(final String type, final Context ctx) {
        assert type != null;
        assert ctx != null;

        log.trace("Creating filter: {}", type);

        FilterNode filter = new FilterNode();
        filter.setType(type);
        filter.setConfiguration(ctx);

        log.trace("Created: {}", filter);

        return filter;
    }
}