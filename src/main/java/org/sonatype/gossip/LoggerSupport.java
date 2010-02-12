/*
 * Copyright (C) 2009 the original author or authors.
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
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;

/**
 * Support for the Gossip {@link org.slf4j.Logger} implementation.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class LoggerSupport
    extends MarkerIgnoringBase
    implements org.slf4j.Logger, Serializable
{
    private static final long serialVersionUID = 1;

    private final String name;

    protected LoggerSupport(final String name) {
        assert name != null;
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getName() + "]@" + System.identityHashCode(this);
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Logger that = (Logger) obj;

        return !(name != null ? !name.equals(that.getName()) : that.getName() != null);
    }

    protected abstract boolean isEnabled(final Gossip.Level level);

    protected abstract void doLog(Gossip.Level level, String message, Throwable cause);

    private void log(final Gossip.Level level, final String msg, final Throwable cause) {
        if (isEnabled(level)) {
            doLog(level, msg, cause);
        }
    }

    private void log(final Gossip.Level level, final String msg) {
        if (isEnabled(level)) {
            doLog(level, msg, null);
        }
    }

    private void log(final Gossip.Level level, final String format, final Object arg) {
        if (isEnabled(level)) {
            if (arg instanceof Throwable) {
                doLog(level, MessageFormatter.format(format, arg), (Throwable) arg);
            }
            else {
                doLog(level, MessageFormatter.format(format, arg), null);
            }
        }
    }

    private void log(final Gossip.Level level, final String format, final Object arg1, final Object arg2) {
        if (isEnabled(level)) {
            if (arg2 instanceof Throwable) {
                doLog(level, MessageFormatter.format(format, arg1, arg2), (Throwable) arg2);
            }
            else {
                doLog(level, MessageFormatter.format(format, arg1, arg2), null);
            }
        }
    }

    private void log(final Gossip.Level level, final String format, final Object[] args) {
        if (isEnabled(level)) {
            if (args != null && args.length != 0 && args[args.length - 1] instanceof Throwable) {
                doLog(level, MessageFormatter.arrayFormat(format, args), (Throwable) args[args.length - 1]);
            }
            else {
                doLog(level, MessageFormatter.arrayFormat(format, args), null);
            }
        }
    }

    //
    // TRACE
    //

    public final boolean isTraceEnabled() {
        return isEnabled(Gossip.Level.TRACE);
    }

    public final void trace(final String msg) {
        log(Gossip.Level.TRACE, msg);
    }

    public final void trace(final String format, final Object arg) {
        log(Gossip.Level.TRACE, format, arg);
    }

    public final void trace(final String format, final Object arg1, final Object arg2) {
        log(Gossip.Level.TRACE, format, arg1, arg2);
    }

    public final void trace(final String format, final Object... args) {
        log(Gossip.Level.TRACE, format, args);
    }

    public final void trace(final String msg, final Throwable cause) {
        log(Gossip.Level.TRACE, msg, cause);
    }

    //
    // DEBUG
    //

    public final boolean isDebugEnabled() {
        return isEnabled(Gossip.Level.DEBUG);
    }

    public final void debug(final String msg) {
        log(Gossip.Level.DEBUG, msg);
    }

    public final void debug(final String format, final Object arg) {
        log(Gossip.Level.DEBUG, format, arg);
    }

    public final void debug(final String format, final Object arg1, final Object arg2) {
        log(Gossip.Level.DEBUG, format, arg1, arg2);
    }

    public final void debug(final String format, final Object... args) {
        log(Gossip.Level.DEBUG, format, args);
    }

    public final void debug(final String msg, final Throwable cause) {
        log(Gossip.Level.DEBUG, msg, cause);
    }

    //
    // INFO
    //

    public final boolean isInfoEnabled() {
        return isEnabled(Gossip.Level.INFO);
    }

    public final void info(final String msg) {
        log(Gossip.Level.INFO, msg);
    }

    public final void info(final String format, final Object arg) {
        log(Gossip.Level.INFO, format, arg);
    }

    public final void info(final String format, final Object arg1, final Object arg2) {
        log(Gossip.Level.INFO, format, arg1, arg2);
    }

    public final void info(final String format, final Object... args) {
        log(Gossip.Level.INFO, format, args);
    }

    public final void info(final String msg, final Throwable cause) {
        log(Gossip.Level.INFO, msg, cause);
    }

    //
    // WARN
    //

    public final boolean isWarnEnabled() {
        return isEnabled(Gossip.Level.WARN);
    }

    public final void warn(final String msg) {
        log(Gossip.Level.WARN, msg);
    }

    public final void warn(final String format, final Object arg) {
        log(Gossip.Level.WARN, format, arg);
    }

    public final void warn(final String format, final Object... args) {
        log(Gossip.Level.WARN, format, args);
    }

    public final void warn(final String format, final Object arg1, final Object arg2) {
        log(Gossip.Level.WARN, format, arg1, arg2);
    }

    public final void warn(final String msg, final Throwable cause) {
        log(Gossip.Level.WARN, msg, cause);
    }

    //
    // ERROR
    //

    public final boolean isErrorEnabled() {
        return isEnabled(Gossip.Level.ERROR);
    }

    public final void error(final String msg) {
        log(Gossip.Level.ERROR, msg);
    }

    public final void error(final String format, final Object arg) {
        log(Gossip.Level.ERROR, format, arg);
    }

    public final void error(final String format, final Object arg1, final Object arg2) {
        log(Gossip.Level.ERROR, format, arg1, arg2);
    }

    public final void error(final String format, final Object... args) {
        log(Gossip.Level.ERROR, format, args);
    }

    public final void error(final String msg, final Throwable cause) {
        log(Gossip.Level.ERROR, msg, cause);
    }
}