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

    private static int nameWidth = -1;

    //
    // TODO: Should probably just re-use the SimpleRenderer to get basic formatting muck
    //
    
    static {
        String tmp;

        tmp = System.getProperty(Log.class.getName() + ".level");

        if (tmp != null) {
            level = Level.valueOf(tmp);
        }

        tmp = System.getProperty(Log.class.getName() + ".nameWidth");

        if (tmp != null) {
            try {
                nameWidth = Integer.parseInt(tmp);
            }
            catch (NumberFormatException e) {
                throw new Error("Invalid Log.nameWidth value: " + tmp, e);
            }
        }
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

    protected boolean isEnabled(final Level l) {
        assert l != null;
        
        return level.id <= l.id;
    }

    protected void doLog(final Level level, final String message, final Throwable cause) {
        assert message != null;
        // cause may be null
        // level should have been checked already

        final PrintStream out = System.out;

        synchronized (out) {
            out.print("[");
            out.print(level.name());
            out.print("] ");

            switch (level) {
                case INFO:
                case WARN:
                    out.print(" ");
            }

            if (nameWidth > 0) {
                out.print(right(getName(), nameWidth));
            }
            else {
                out.print(getName());
            }
            out.print(" - ");

            out.println(message);

            if (cause != null) {
                cause.printStackTrace(out);
            }

            out.flush();
        }
    }

    private static String right(final String str, final int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if (str == null || str.length() <= len) {
            return str;
        }
        else {
            return str.substring(str.length() - len);
        }
    }
}