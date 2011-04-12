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

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * {@link OutputStream} which logs lines to a {@link Logger}.
 *
 * Optionally will delegate to another {@link OutputStream} after logging if given instance is non-null.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.8
 */
public class LoggingOutputStream
    extends FilterOutputStream
{
    private final Logger logger;

    private final Level level;

    private final ByteArrayOutputStream buff = new ByteArrayOutputStream(1024 * 4);

    private int last;

    public LoggingOutputStream(final Logger logger, final Level level, final OutputStream delegate) {
        super(delegate);
        assert logger != null;
        this.logger = logger;
        assert level != null;
        this.level = level;
    }

    public LoggingOutputStream(final Logger logger, final Level level) {
        this(logger, level, null);
    }

    public LoggingOutputStream(final Logger logger) {
        this(logger, Level.INFO);
    }

    public Logger getLogger() {
        return logger;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public void write(final int b) throws IOException {
        if (b == '\r' || b == '\n') {
            if (!(last != '\r' || b != '\n')) {
                level.log(logger, buff.toString("UTF-8"));
                buff.reset();
            }
        }
        else {
            buff.write(b);
        }
        last = b;

        if (out != null) {
            super.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        if (out != null) {
            super.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (out != null) {
            super.close();
        }
    }
}