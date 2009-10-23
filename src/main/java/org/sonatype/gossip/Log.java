/*
 * Copyright (C) 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.gossip;

import org.sonatype.gossip.render.BasicRenderer;

import java.io.PrintStream;

/**
 * Provides internal logging support for Gossip.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public final class Log
    extends LoggerSupport
{
    private static Level level = Level.WARN;

    private static BasicRenderer renderer;

    static {
        String tmp = System.getProperty(Log.class.getName() + ".level");
        if (tmp != null) {
            level = Level.valueOf(tmp.toUpperCase());
        }

        renderer = new BasicRenderer();
        renderer.setIncludeName(true);
    }

    public Log(final String name) {
        super(name);
    }

    public static Log getLogger(final String name) {
        assert name != null;

        return new Log(name);
    }
    
    public static Log getLogger(final Class type) {
        assert type != null;

        return new Log(type.getName());
    }

    @Override
    protected boolean isEnabled(final Level l) {
        assert l != null;
        
        return level.id <= l.id;
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