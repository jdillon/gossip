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
package com.planet57.gossip;

import org.slf4j.Logger;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Gossip logging level.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public enum Level
{
    ALL(-1000),                             // -1000

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

    /**
     * @since 1.8
     */
    public boolean isEnabled(final Logger logger) {
        assert logger != null;
        switch (this) {
            case ALL:
                return true;
            case TRACE:
                return logger.isTraceEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case ERROR:
                return logger.isErrorEnabled();
            default:
                return false;
        }
    }

    /**
     * @since 1.8
     */
    public void log(final Logger logger, final String msg) {
        assert logger != null;
        switch (this) {
            case TRACE:
                logger.trace(msg);
                break;
            case DEBUG:
                logger.debug(msg);
                break;
            case INFO:
                logger.info(msg);
                break;
            case WARN:
                logger.warn(msg);
                break;
            case ERROR:
                logger.error(msg);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * @since 1.8
     */
    public void log(final Logger logger, final String format, final Object arg) {
        assert logger != null;
        switch (this) {
            case TRACE:
                logger.trace(format, arg);
                break;
            case DEBUG:
                logger.debug(format, arg);
                break;
            case INFO:
                logger.info(format, arg);
                break;
            case WARN:
                logger.warn(format, arg);
                break;
            case ERROR:
                logger.error(format, arg);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * @since 1.8
     */
    public void log(final Logger logger, final String format, final Object arg1, final Object arg2) {
        assert logger != null;
        switch (this) {
            case TRACE:
                logger.trace(format, arg1, arg2);
                break;
            case DEBUG:
                logger.debug(format, arg1, arg2);
                break;
            case INFO:
                logger.info(format, arg1, arg2);
                break;
            case WARN:
                logger.warn(format, arg1, arg2);
                break;
            case ERROR:
                logger.error(format, arg1, arg2);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * @since 1.8
     */
    public void log(final Logger logger, final String format, final Object... args) {
        assert logger != null;
        switch (this) {
            case TRACE:
                logger.trace(format, args);
                break;
            case DEBUG:
                logger.debug(format, args);
                break;
            case INFO:
                logger.info(format, args);
                break;
            case WARN:
                logger.warn(format, args);
                break;
            case ERROR:
                logger.error(format, args);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * @since 1.8
     */
    public void log(final Logger logger, final String msg, final Throwable cause) {
        assert logger != null;
        switch (this) {
            case TRACE:
                logger.trace(msg, cause);
                break;
            case DEBUG:
                logger.debug(msg, cause);
                break;
            case INFO:
                logger.info(msg, cause);
                break;
            case WARN:
                logger.warn(msg, cause);
                break;
            case ERROR:
                logger.error(msg, cause);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}