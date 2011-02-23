/**
 * Copyright (c) 2009-2011 the original author or authors.
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

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.sonatype.gossip.LoggerDelegateFactory.LoggerDelegateAware;
import org.sonatype.gossip.render.PatternRenderer;
import org.sonatype.gossip.render.Renderer;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides internal logging support for Gossip.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public final class Log
{
    private final static Map<String,LoggerDelegateAware> delegates = new HashMap<String,LoggerDelegateAware>();

    private static final String INTERNAL_PREFIX = "org.sonatype.gossip";

    private static volatile Level threshold;

    private static final Level internalThreshold;

    private static volatile Renderer renderer;

    private static volatile PrintStream stream;

    private static boolean configured;

    private static ILoggerFactory configuredFactory;

    private static enum StreamType
    {
        OUT,
        ERR
    }

    static {
        final String baseName = Log.class.getName();

        String thresholdName = System.getProperty(baseName + ".threshold");
        threshold = thresholdName != null ? Level.valueOf(thresholdName.toUpperCase()) : Level.WARN;

        String internalThresholdName = System.getProperty(baseName + ".internalThreshold");
        internalThreshold = internalThresholdName != null ? Level.valueOf(internalThresholdName.toUpperCase()) : Level.WARN;

        // Use a pattern renderer by default, user can install whatever they want later
        PatternRenderer renderer = new PatternRenderer();
        String pattern = System.getProperty(baseName + ".pattern", PatternRenderer.DEFAULT_PATTERN);
        renderer.setPattern(pattern);
        Log.renderer = renderer;

        StreamType streamType = StreamType.OUT;
        String streamName = System.getProperty(baseName + ".stream");
        if (streamName != null) {
            streamType = StreamType.valueOf(streamName.toUpperCase());
        }
        switch (streamType) {
            case ERR:
                stream = System.err;
                break;
            default:
            case OUT:
                stream = System.out;
                break;
        }
    }

    /**
     * @since 1.6
     */
    public static Level getThreshold() {
        return threshold;
    }

    /**
     * @since 1.6
     */
    public static void setThreshold(final Level threshold) {
        if (threshold == null) {
            throw new NullPointerException();
        }
        Log.threshold = threshold;
    }

    /**
     * @since 1.6
     */
    public static Renderer getRenderer() {
        return renderer;
    }

    /**
     * @since 1.6
     */
    public static void setRenderer(final Renderer renderer) {
        if (renderer == null) {
            throw new NullPointerException();
        }
        Log.renderer = renderer;
    }

    /**
     * @since 1.6
     */
    public static PrintStream getStream() {
        return stream;
    }

    /**
     * @since 1.6
     */
    public static void setStream(final PrintStream stream) {
        if (stream == null) {
            throw new NullPointerException();
        }
        Log.stream = stream;
    }

    public static synchronized void configure(final ILoggerFactory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        if (!configured) {
            configuredFactory = factory;

            // Replace all logger delegates with real loggers
            for (Map.Entry<String,LoggerDelegateAware> entry : delegates.entrySet()) {
                Logger logger = configuredFactory.getLogger(entry.getKey());
                entry.getValue().setDelegate(logger);
            }
            delegates.clear();
            configured = true;
        }
    }

    public static synchronized Logger getLogger(final String name) {
        assert name != null;

        // Gossip loggers will always be internal
        if (name.startsWith(INTERNAL_PREFIX)) {
            return new LoggerImpl(name);
        }

        if (!configured) {
            Logger delegate = LoggerDelegateFactory.create(new LoggerImpl(name));
            delegates.put(name, (LoggerDelegateAware)delegate);
            return delegate;
        }

        return configuredFactory.getLogger(name);
    }
    
    public static Logger getLogger(final Class type) {
        assert type != null;
        return getLogger(type.getName());
    }

    private static class LoggerImpl
        extends LoggerSupport
    {
        private LoggerImpl(final String name) {
            super(name);
        }

        @Override
        protected boolean isEnabled(final Level l) {
            assert l != null;

            Level threshold = getThreshold();
            if (getName().startsWith(INTERNAL_PREFIX)) {
                threshold = Log.internalThreshold;
            }

            return threshold.id <= l.id;
        }


        @Override
        protected void doLog(final Event event) {
            final PrintStream out = getStream();
            synchronized (out) {
                out.print(getRenderer().render(event));
                out.flush();
            }
        }
    }
}