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

    private final static PatternRenderer renderer;

    private static volatile PrintStream out;

    private static volatile Level threshold;

    private static final Level internalThreshold;

    private static boolean configured;

    private static ILoggerFactory configuredFactory;

    static {
        out = System.out;
        String baseName = Log.class.getName();
        String defaultThreshold = Level.WARN.toString();
        threshold = Level.valueOf(System.getProperty(baseName + ".threshold", defaultThreshold).toUpperCase());
        internalThreshold = Level.valueOf(System.getProperty(baseName + ".internalThreshold", defaultThreshold).toUpperCase());
        renderer = new PatternRenderer();
        String pattern = System.getProperty(baseName + ".pattern", PatternRenderer.DEFAULT_PATTERN);
        renderer.setPattern(pattern);
    }

    /**
     * @since 1.6
     */
    public static PrintStream getOut() {
        return out;
    }

    /**
     * @since 1.6
     */
    public static void setOut(final PrintStream out) {
        if (out == null) {
            throw new NullPointerException();
        }
        Log.out = out;
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
        protected void doLog(final Level level, final String message, final Throwable cause) {
            assert message != null;
            // cause may be null
            // level should have been checked already

            final PrintStream out = getOut();
            synchronized (out) {
                out.print(renderer.render(new Event(this, level, message, cause)));
                out.flush();
            }
        }
    }
}