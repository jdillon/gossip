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

package org.sonatype.gossip.render;

import org.sonatype.gossip.Event;

/**
 * Renders events based on a pattern.
 * <p/>
 * Pattern syntax:
 * <pre>
 *   ((<tt>%</tt><em>token</em>)*(text)*)+
 * </pre>
 *
 * <table>               
 *   <tr>
 *     <td><tt>%</tt></td>
 *     <td>The sequence %% outputs a single percent sign.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>d</tt></td>
 *     <td>Renders the time-stamp.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>c</tt></td>
 *     <td>Renders the short logger name.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>C</tt></td>
 *     <td>Renders the full logger name.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>l</tt></td>
 *     <td>Renders the level.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>t</tt></td>
 *     <td>Renders the thread name.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>T</tt></td>
 *     <td>Renders the trace.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>m</tt></td>
 *     <td>Renders the message.</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>x</tt></td>
 *     <td>Renders the cause.</td>
 *   </tr>
 * 
 *   <tr>
 *     <td><tt>n</tt></td>
 *     <td>Renders a newline.</td>
 *   </tr>
 * </table>
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class PatternRenderer
    implements Renderer
{
    private static final String NEWLINE = System.getProperty("line.separator");

    private String pattern = "[%l] %c - %m%n%x";

    @Override
    public String toString() {
        return "PatternRenderer{" +
            "pattern=" + pattern +
            '}';
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(final String pattern) {
        assert pattern != null;
        this.pattern = pattern;
    }

    public String render(final Event event) {
        assert event != null;
        assert pattern != null;

        StringBuilder buff = new StringBuilder();

        int len = pattern.length();
        for (int i=0; i < len; i++) {
            char c = pattern.charAt(i);
            if (c == '%') {
                if (i + 1 >= len) {
                    throw new IllegalArgumentException("Invalid pattern: " + pattern);
                }
                char t = pattern.charAt(++i);

                switch (t) {
                    case '%':
                        buff.append(t);
                        break;

                    case 'd':
                        renderTimeStamp(event, buff);
                        break;

                    case 'c':
                        renderName(event, buff, true);
                        break;

                    case 'C':
                        renderName(event, buff, false);
                        break;

                    case 'l':
                        renderLevel(event, buff);
                        break;

                    case 't':
                        renderThreadName(event, buff);
                        break;

                    case 'T':
                        renderTrace(event, buff);
                        break;

                    case 'm':
                        renderMessage(event, buff);
                        break;

                    case 'x':
                        if (event.getCause() != null) {
                            renderCause(event, buff);
                        }
                        break;

                    case 'n':
                        renderNewLine(buff);
                        break;

                    //
                    // TODO: Add tokens to render details about where the log line was emitted.
                    //
                    
                    default:
                        throw new IllegalArgumentException("Invalid pattern token: " + t);
                }
            }
            else {
                buff.append(c);
            }
        }

        return buff.toString();
    }

    protected void renderNewLine(final StringBuilder buff) {
        assert buff != null;
        buff.append(NEWLINE);
    }

    protected void renderTimeStamp(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getTimeStamp());
    }

    protected void renderLevel(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getLevel().name());
    }

    protected void renderName(final Event event, final StringBuilder buff, final boolean shortName) {
        assert event != null;
        assert buff != null;

        String name = event.getName();

        if (shortName) {
            int i = name.lastIndexOf(".");

            if (i != -1) {
                name = name.substring(i + 1, name.length());
            }
        }

        buff.append(name);
    }

    protected void renderThreadName(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getThreadName());
    }

    protected void renderMessage(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getMessage());
    }

    protected void renderCause(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        Throwable cause = event.getCause();

        buff.append(cause);
        buff.append(NEWLINE);

        while (cause != null) {
            for (StackTraceElement e : cause.getStackTrace()) {
                buff.append("    at ").append(e.getClassName()).append(".").append(e.getMethodName());
                buff.append(" (").append(getLocation(e)).append(")");
                buff.append(NEWLINE);
            }

            cause = cause.getCause();
            if (cause != null) {
                buff.append("Caused by ").append(cause.getClass().getName()).append(": ");
                buff.append(cause.getMessage());
                buff.append(NEWLINE);
            }
        }
    }

    protected void renderTrace(final Event event, final StringBuilder buff) {
        // TODO:
    }

    protected String getLocation(final StackTraceElement e) {
        assert e != null;

        if (e.isNativeMethod()) {
            return "Native Method";
        }
        else if (e.getFileName() == null) {
            return "Unknown Source";
        }
        else if (e.getLineNumber() >= 0) {
            return String.format("%s:%s", e.getFileName(), e.getLineNumber());
        }
        else {
            return e.getFileName();
        }
    }
}