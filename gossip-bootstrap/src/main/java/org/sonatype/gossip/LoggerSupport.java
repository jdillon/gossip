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
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;

/**
 * Support for the Gossip {@link Logger} implementation.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
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

    protected abstract boolean isEnabled(final Level level);

    protected abstract void doLog(Level level, String message, Throwable cause);

    private void log(final Level level, final FormattingTuple tuple) {
        doLog(level, tuple.getMessage(), tuple.getThrowable());
    }

    private void log(final Level level, final String msg) {
        if (isEnabled(level)) {
            doLog(level, msg, null);
        }
    }

    private void log(final Level level, final String format, final Object arg) {
        if (isEnabled(level)) {
            log(level, MessageFormatter.format(format, arg));
        }
    }

    private void log(final Level level, final String format, final Object arg1, final Object arg2) {
        if (isEnabled(level)) {
            log(level, MessageFormatter.format(format, arg1, arg2));
        }
    }

    private void log(final Level level, final String format, final Object[] args) {
        if (isEnabled(level)) {
            log(level, MessageFormatter.arrayFormat(format, args));
        }
    }

    //
    // TRACE
    //

    public final boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    public final void trace(final String msg) {
        log(Level.TRACE, msg);
    }

    public final void trace(final String format, final Object arg) {
        log(Level.TRACE, format, arg);
    }

    public final void trace(final String format, final Object arg1, final Object arg2) {
        log(Level.TRACE, format, arg1, arg2);
    }

    public final void trace(final String format, final Object... args) {
        log(Level.TRACE, format, args);
    }

    public final void trace(final String msg, final Throwable cause) {
        log(Level.TRACE, msg, cause);
    }

    //
    // DEBUG
    //

    public final boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    public final void debug(final String msg) {
        log(Level.DEBUG, msg);
    }

    public final void debug(final String format, final Object arg) {
        log(Level.DEBUG, format, arg);
    }

    public final void debug(final String format, final Object arg1, final Object arg2) {
        log(Level.DEBUG, format, arg1, arg2);
    }

    public final void debug(final String format, final Object... args) {
        log(Level.DEBUG, format, args);
    }

    public final void debug(final String msg, final Throwable cause) {
        log(Level.DEBUG, msg, cause);
    }

    //
    // INFO
    //

    public final boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    public final void info(final String msg) {
        log(Level.INFO, msg);
    }

    public final void info(final String format, final Object arg) {
        log(Level.INFO, format, arg);
    }

    public final void info(final String format, final Object arg1, final Object arg2) {
        log(Level.INFO, format, arg1, arg2);
    }

    public final void info(final String format, final Object... args) {
        log(Level.INFO, format, args);
    }

    public final void info(final String msg, final Throwable cause) {
        log(Level.INFO, msg, cause);
    }

    //
    // WARN
    //

    public final boolean isWarnEnabled() {
        return isEnabled(Level.WARN);
    }

    public final void warn(final String msg) {
        log(Level.WARN, msg);
    }

    public final void warn(final String format, final Object arg) {
        log(Level.WARN, format, arg);
    }

    public final void warn(final String format, final Object... args) {
        log(Level.WARN, format, args);
    }

    public final void warn(final String format, final Object arg1, final Object arg2) {
        log(Level.WARN, format, arg1, arg2);
    }

    public final void warn(final String msg, final Throwable cause) {
        log(Level.WARN, msg, cause);
    }

    //
    // ERROR
    //

    public final boolean isErrorEnabled() {
        return isEnabled(Level.ERROR);
    }

    public final void error(final String msg) {
        log(Level.ERROR, msg);
    }

    public final void error(final String format, final Object arg) {
        log(Level.ERROR, format, arg);
    }

    public final void error(final String format, final Object arg1, final Object arg2) {
        log(Level.ERROR, format, arg1, arg2);
    }

    public final void error(final String format, final Object... args) {
        log(Level.ERROR, format, args);
    }

    public final void error(final String msg, final Throwable cause) {
        log(Level.ERROR, msg, cause);
    }
}