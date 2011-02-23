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

package org.sonatype.gossip.support;

import org.slf4j.Logger;
import org.sonatype.gossip.Level;
import org.sonatype.gossip.LoggerSupport;

import java.io.PrintStream;

/**
 * Adapts a {@link PrintStream} to a {@link Logger} interface.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public class PrintStreamLogger
    extends LoggerSupport
{
    private final PrintStream stream;

    private Level level;

    public PrintStreamLogger(final PrintStream stream, final Level level) {
        assert stream != null;
        this.stream = stream;
        this.level = level;
    }

    public PrintStreamLogger(final PrintStream stream) {
        this(stream, null);
    }

    public PrintStream getStream() {
        return stream;
    }

    public Level getLevel() {
        if (level == null) {
            return Level.TRACE;
        }
        return level;
    }

    public void setLevel(final Level level) {
        // null will reset to default
        this.level = level;
    }

    public void setName(final String name) {
        // null will reset to default
        this.name = name;
    }

    protected boolean isEnabled(final Level level) {
        assert level != null;
        return getLevel().id <= level.id;
    }

    @Override
    protected void doLog(final Level level, final String message, final Throwable cause) {
        synchronized (stream) {
            stream.print("[");
            stream.print(level);
            stream.print("] ");
            if (name != null) {
                stream.print(name);
                stream.print(": ");
            }
            stream.println(message);
            if (cause != null) {
                cause.printStackTrace(stream);
            }
            stream.flush();
        }
    }
}