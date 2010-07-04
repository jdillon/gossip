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
import org.slf4j.Marker;

/**
 * A logger which delegates to another logger.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public final class LoggerDelegate
    implements Logger
{
    private Logger delegate;

    public LoggerDelegate(final Logger delegate) {
        setDelegate(delegate);
    }

    public Logger getDelegate() {
        return delegate;
    }

    public void setDelegate(final Logger delegate) {
        if (delegate == null) throw new IllegalArgumentException();
        this.delegate = delegate;
    }

    public String getName() {
        return getDelegate().getName();
    }

    public boolean isTraceEnabled() {
        return getDelegate().isTraceEnabled();
    }

    public void trace(String msg) {
        getDelegate().trace(msg);
    }

    public void trace(String format, Object arg) {
        getDelegate().trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        getDelegate().trace(format, arg1, arg2);
    }

    public void trace(String format, Object[] argArray) {
        getDelegate().trace(format, argArray);
    }

    public void trace(String msg, Throwable t) {
        getDelegate().trace(msg, t);
    }

    public boolean isTraceEnabled(Marker marker) {
        return getDelegate().isTraceEnabled(marker);
    }

    public void trace(Marker marker, String msg) {
        getDelegate().trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        getDelegate().trace(marker, format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        getDelegate().trace(marker, format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object[] argArray) {
        getDelegate().trace(marker, format, argArray);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        getDelegate().trace(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        return getDelegate().isDebugEnabled();
    }

    public void debug(String msg) {
        getDelegate().debug(msg);
    }

    public void debug(String format, Object arg) {
        getDelegate().debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        getDelegate().debug(format, arg1, arg2);
    }

    public void debug(String format, Object[] argArray) {
        getDelegate().debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
        getDelegate().debug(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return getDelegate().isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        getDelegate().debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        getDelegate().debug(marker, format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        getDelegate().debug(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object[] argArray) {
        getDelegate().debug(marker, format, argArray);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        getDelegate().debug(marker, msg, t);
    }

    public boolean isInfoEnabled() {
        return getDelegate().isInfoEnabled();
    }

    public void info(String msg) {
        getDelegate().info(msg);
    }

    public void info(String format, Object arg) {
        getDelegate().info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        getDelegate().info(format, arg1, arg2);
    }

    public void info(String format, Object[] argArray) {
        getDelegate().info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        getDelegate().info(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return getDelegate().isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {
        getDelegate().info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg) {
        getDelegate().info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        getDelegate().info(marker, format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object[] argArray) {
        getDelegate().info(marker, format, argArray);
    }

    public void info(Marker marker, String msg, Throwable t) {
        getDelegate().info(marker, msg, t);
    }

    public boolean isWarnEnabled() {
        return getDelegate().isWarnEnabled();
    }

    public void warn(String msg) {
        getDelegate().warn(msg);
    }

    public void warn(String format, Object arg) {
        getDelegate().warn(format, arg);
    }

    public void warn(String format, Object[] argArray) {
        getDelegate().warn(format, argArray);
    }

    public void warn(String format, Object arg1, Object arg2) {
        getDelegate().warn(format, arg1, arg2);
    }

    public void warn(String msg, Throwable t) {
        getDelegate().warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return getDelegate().isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg) {
        getDelegate().warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        getDelegate().warn(marker, format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        getDelegate().warn(marker, format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object[] argArray) {
        getDelegate().warn(marker, format, argArray);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        getDelegate().warn(marker, msg, t);
    }

    public boolean isErrorEnabled() {
        return getDelegate().isErrorEnabled();
    }

    public void error(String msg) {
        getDelegate().error(msg);
    }

    public void error(String format, Object arg) {
        getDelegate().error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        getDelegate().error(format, arg1, arg2);
    }

    public void error(String format, Object[] argArray) {
        getDelegate().error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        getDelegate().error(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return getDelegate().isErrorEnabled(marker);
    }

    public void error(Marker marker, String msg) {
        getDelegate().error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg) {
        getDelegate().error(marker, format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        getDelegate().error(marker, format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object[] argArray) {
        getDelegate().error(marker, format, argArray);
    }

    public void error(Marker marker, String msg, Throwable t) {
        getDelegate().error(marker, msg, t);
    }
}