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

import org.slf4j.Logger;
import org.sonatype.gossip.Gossip.Level;
import org.sonatype.gossip.render.PatternRenderer;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.sonatype.gossip.Gossip.Level.WARN;

/**
 * Provides internal logging support for Gossip.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public final class Log
{
    private final static Map<String,LoggerDelegate> delegates = new HashMap<String,LoggerDelegate>();

    private static final String INTERNAL_PREFIX = "org.sonatype.gossip";

    private final static Level level;

    private final static Level internalLevel;

    private final static PatternRenderer renderer;

    private static boolean configured;

    static {
        level = Level.valueOf(System.getProperty(Log.class.getName() + ".level", WARN.toString()).toUpperCase());
        internalLevel = Level.valueOf(System.getProperty(Log.class.getName() + ".internal.level", WARN.toString()).toUpperCase());
        renderer = new PatternRenderer();
    }

    static void configure() {
        if (!configured) {
            // Replace all logger delegates with real loggers
            for (Map.Entry<String,LoggerDelegate> entry : delegates.entrySet()) {
                Logger logger = Gossip.getInstance().getLogger(entry.getKey());
                entry.getValue().setDelegate(logger);
            }
            delegates.clear();
            configured = true;
        }
    }

    public static Logger getLogger(final String name) {
        assert name != null;

        // Gossip loggers will always be internal
        if (name.startsWith(INTERNAL_PREFIX)) {
            return new LoggerImpl(name);
        }

        if (!configured) {
            synchronized (delegates) {
                LoggerDelegate delegate = new LoggerDelegate(new LoggerImpl(name));
                delegates.put(name, delegate);
                return delegate;
            }
        }

        return Gossip.getInstance().getLogger(name);
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

            Level threshold = Log.level;

            if (getName().startsWith(INTERNAL_PREFIX)) {
                threshold = Log.internalLevel;
            }

            return threshold.id <= l.id;
        }

        @Override
        protected void doLog(final Level level, final String message, final Throwable cause) {
            assert message != null;
            // cause may be null
            // level should have been checked already

            final PrintStream out = System.out;

            synchronized (out) {
                out.print(renderer.render(new Event(this, level, message, cause)));
                out.flush();
            }
        }
    }
}