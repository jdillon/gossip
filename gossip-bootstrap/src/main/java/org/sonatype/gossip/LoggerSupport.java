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

import static org.sonatype.gossip.Level.DEBUG;
import static org.sonatype.gossip.Level.ERROR;
import static org.sonatype.gossip.Level.INFO;
import static org.sonatype.gossip.Level.TRACE;
import static org.sonatype.gossip.Level.WARN;

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

    protected LoggerSupport(final String name) {
        this.name = name;
    }

    protected LoggerSupport() {
        // nop
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append(getClass().getSimpleName());
        if (name != null) {
            buff.append("[").append(name).append("]");
        }
        buff.append(String.format("@%x", System.identityHashCode(this)));
        return buff.toString();
    }

    @Override
    public final int hashCode() {
        if (name == null) {
            return System.identityHashCode(this);
        }
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
        return isEnabled(TRACE);
    }

    public final void trace(final String msg) {
        log(TRACE, msg);
    }

    public final void trace(final String format, final Object arg) {
        log(TRACE, format, arg);
    }

    public final void trace(final String format, final Object arg1, final Object arg2) {
        log(TRACE, format, arg1, arg2);
    }

    public final void trace(final String format, final Object... args) {
        log(TRACE, format, args);
    }

    public final void trace(final String msg, final Throwable cause) {
        log(TRACE, msg, cause);
    }

    //
    // DEBUG
    //

    public final boolean isDebugEnabled() {
        return isEnabled(DEBUG);
    }

    public final void debug(final String msg) {
        log(DEBUG, msg);
    }

    public final void debug(final String format, final Object arg) {
        log(DEBUG, format, arg);
    }

    public final void debug(final String format, final Object arg1, final Object arg2) {
        log(DEBUG, format, arg1, arg2);
    }

    public final void debug(final String format, final Object... args) {
        log(DEBUG, format, args);
    }

    public final void debug(final String msg, final Throwable cause) {
        log(DEBUG, msg, cause);
    }

    //
    // INFO
    //

    public final boolean isInfoEnabled() {
        return isEnabled(INFO);
    }

    public final void info(final String msg) {
        log(INFO, msg);
    }

    public final void info(final String format, final Object arg) {
        log(INFO, format, arg);
    }

    public final void info(final String format, final Object arg1, final Object arg2) {
        log(INFO, format, arg1, arg2);
    }

    public final void info(final String format, final Object... args) {
        log(INFO, format, args);
    }

    public final void info(final String msg, final Throwable cause) {
        log(INFO, msg, cause);
    }

    //
    // WARN
    //

    public final boolean isWarnEnabled() {
        return isEnabled(WARN);
    }

    public final void warn(final String msg) {
        log(WARN, msg);
    }

    public final void warn(final String format, final Object arg) {
        log(WARN, format, arg);
    }

    public final void warn(final String format, final Object... args) {
        log(WARN, format, args);
    }

    public final void warn(final String format, final Object arg1, final Object arg2) {
        log(WARN, format, arg1, arg2);
    }

    public final void warn(final String msg, final Throwable cause) {
        log(WARN, msg, cause);
    }

    //
    // ERROR
    //

    public final boolean isErrorEnabled() {
        return isEnabled(ERROR);
    }

    public final void error(final String msg) {
        log(ERROR, msg);
    }

    public final void error(final String format, final Object arg) {
        log(ERROR, format, arg);
    }

    public final void error(final String format, final Object arg1, final Object arg2) {
        log(ERROR, format, arg1, arg2);
    }

    public final void error(final String format, final Object... args) {
        log(ERROR, format, args);
    }

    public final void error(final String msg, final Throwable cause) {
        log(ERROR, msg, cause);
    }
}