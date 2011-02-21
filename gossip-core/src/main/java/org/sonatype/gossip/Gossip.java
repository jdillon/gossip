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
import org.slf4j.spi.LocationAwareLogger;
import org.sonatype.gossip.model.LoggerNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory to produce <em>Gossip</em> {@link org.slf4j.Logger} instances.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public final class Gossip
    implements ILoggerFactory
{
    private static final org.slf4j.Logger log = Log.getLogger(Gossip.class);

    private static final Gossip INSTANCE = new Gossip();

    public static Gossip getInstance() {
        return INSTANCE;
    }

    /**
     * Map {@link org.slf4j.Logger} names to {@link Gossip.Logger} or {@link ProvisionNode}.
     */
    private final Map<String,Loggerish> loggers = new HashMap<String,Loggerish>();

    private final Logger root = new Logger(Logger.ROOT_NAME, Level.WARN);

    private final EffectiveProfile effectiveProfile;

    private Gossip() {
        if (log.isTraceEnabled()) {
            //noinspection ThrowableInstanceNeverThrown
            log.trace("Initializing", new Throwable("INIT MARKER"));
        }

        effectiveProfile = new Configurator().configure();
        prime();
    }

    public Logger getRoot() {
        return root;
    }

    public EffectiveProfile getEffectiveProfile() {
        return effectiveProfile;
    }

    private void prime() {
        log.trace("Priming");

        // Include the root logger in the list
        loggers.put(root.getName(), root);

        // Prime the loggers we have configured
        for (Map.Entry<String,LoggerNode> entry : getEffectiveProfile().loggers().entrySet()) {
            String name = entry.getKey();
            LoggerNode node = entry.getValue();
            Logger logger;

            if (Logger.ROOT_TOKEN.equals(name)) {
                logger = root;
            }
            else {
                logger = getLogger(name);
            }
            
            logger.setLevel(node.asLevel());
        }
    }

    public Logger getLogger(final String name) {
        assert name != null;

        Logger logger;

        synchronized (loggers) {
            Object obj = loggers.get(name);

            if (obj == null) {
                logger = new Logger(name);
                loggers.put(name, logger);
                log.trace("Created logger: {}", logger);
                updateParents(logger);
            }
            else if (obj instanceof ProvisionNode) {
                ProvisionNode node = (ProvisionNode)obj;
                logger = new Logger(name);
                loggers.put(name, logger);
                log.trace("Replaced provision node with logger: {}", logger);
                updateChildren(node, logger);
                updateParents(logger);
            }
            else if (obj instanceof Logger) {
                logger = (Logger)obj;
                log.trace("Using cached logger: {}", logger);
            }
            else {
                throw new InternalError();
            }
        }

        return logger;
    }

    /**
     * @since 1.4
     */
    public Collection<String> getLoggerNames() {
        synchronized (loggers) {
            return Collections.unmodifiableSet(loggers.keySet());
        }
    }

    /**
     * Gossip logging level.
     *
     * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
     * @since 1.4
     */
    public static enum Level
    {
        TRACE(LocationAwareLogger.TRACE_INT),   // 0

        DEBUG(LocationAwareLogger.DEBUG_INT),   // 10

        INFO(LocationAwareLogger.INFO_INT),     // 20

        WARN(LocationAwareLogger.WARN_INT),     // 30

        ERROR(LocationAwareLogger.ERROR_INT),   // 40

        OFF(1000);                              // 1000

        public final int id;

        private Level(final int id) {
            this.id = id;
        }
    }

    /**
     * Gossip logger.
     *
     * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
     * @since 1.4
     */
    public final class Logger
        extends LoggerSupport
        implements Loggerish
    {
        public static final String ROOT_TOKEN="*";

        public static final String ROOT_NAME=ROOT_LOGGER_NAME;

        private Level level;

        private Level cachedLevel;

        private Logger parent;

        private Logger(final String name, final Level level) {
            super(name);
            this.level = level;
        }

        private Logger(final String name) {
            this(name, null);
        }

        public Logger getParent() {
            return parent;
        }

        public Level getLevel() {
            return level;
        }

        public void setLevel(final Level level) {
            // level can be null
            this.level = level;
            this.cachedLevel = level;
            invalidateCache();
        }

        private void invalidateCache() {
            // Update any children's cached level, forcing them to re-evaluate and cache if needed
            for (Map.Entry<String, Loggerish> entry : loggers.entrySet()) {
                if (entry.getKey().startsWith(getName() + ".")) {
                    Object obj = entry.getValue();
                    if (obj instanceof Gossip.Logger) {
                        Gossip.Logger logger = (Gossip.Logger)obj;
                        if (logger.level == null) {
                            logger.cachedLevel = null;
                        }
                    }
                }
            }
        }

        public Level findEffectiveLevel() {
            for (Logger logger = this; logger != null; logger=logger.parent) {
                if (logger.level != null) {
                    return logger.level;
                }
            }
            return Level.OFF;
        }

        public Level getEffectiveLevel() {
            // If we don't have a cached level, then find and cache it
            if (cachedLevel == null) {
                cachedLevel = findEffectiveLevel();
            }

            return cachedLevel;
        }

        @Override
        protected boolean isEnabled(final Level level) {
            assert level != null;
            return getEffectiveLevel().id <= level.id;
        }

        @Override
        protected void doLog(final Level level, final String message, final Throwable cause) {
            getEffectiveProfile().dispatch(new Event(this, level, message, cause));
        }
        
        @Override
        public String toString() {
            return "Logger[" + getName() + "]@" + System.identityHashCode(this);
        }
    }

    //
    // NOTE: The following was borrowed and massaged from Log4j
    //

    /**
     * Marker interface for the {@link #loggers} map.
     */
    private interface Loggerish
    {
        // Empty
    }

    private final class ProvisionNode
        extends ArrayList<Object>
        implements Loggerish
    {
        private ProvisionNode(final Logger logger) {
            assert logger != null;
            add(logger);
        }
    }

    private void updateParents(final Logger logger) {
        assert logger != null;

        String name = logger.getName();
        int length = name.length();
        boolean parentFound = false;

        // if name = "w.x.y.z", loop through "w.x.y", "w.x" and "w", but not "w.x.y.z"
        for (int i = name.lastIndexOf('.', length - 1); i >= 0; i = name.lastIndexOf('.', i - 1)) {
            String key = name.substring(0, i);

            Object obj = loggers.get(key);

            // Create a provision node for a future parent.
            if (obj == null) {
                ProvisionNode node = new ProvisionNode(logger);
                loggers.put(key, node);
            }
            else if (obj instanceof Logger) {
                parentFound = true;
                logger.parent = (Logger) obj;

                // no need to update the ancestors of the closest ancestor
                break;
            }
            else if (obj instanceof ProvisionNode) {
	            ((ProvisionNode) obj).add(logger);
            }
            else {
                throw new InternalError();
            }
        }

        // If we could not find any existing parents, then link with root.
        if (!parentFound) {
            logger.parent = root;
        }
    }

    private void updateChildren(final ProvisionNode node, final Logger logger) {
        assert node != null;
        assert logger != null;

        final int last = node.size();

        for (int i = 0; i < last; i++) {
            Logger l = (Logger) node.get(i);

            // Unless this child already points to a correct (lower) parent,
            // make cat.parent point to l.parent and l.parent to cat.
            if (!l.parent.getName().startsWith(logger.getName())) {
                logger.parent = l.parent;
                l.parent = logger;
            }
        }
    }
}
