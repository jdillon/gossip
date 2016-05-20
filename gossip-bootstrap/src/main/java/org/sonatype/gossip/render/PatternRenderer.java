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
package org.sonatype.gossip.render;

import org.sonatype.gossip.Event;

/**
 * Renders events based on a pattern.
 * 
 * <p/>
 * Pattern syntax:
 * <pre>
 *   ((<em>token</em>)*(<tt>text</tt>)*)+
 * </pre>
 *
 * <h4>Tokens:</h4>
 *
 * <table border="1" cellspacing="5" cellpadding="5">
 *   <tr>
 *     <td><tt>%%</tt></td>
 *     <td>A percent sign</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%d</tt></td>
 *     <td>Time-stamp</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%c</tt></td>
 *     <td>Short logger name</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%C</tt></td>
 *     <td>Full logger name</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%l</tt></td>
 *     <td>Level</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%t</tt></td>
 *     <td>Thread name</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%m</tt></td>
 *     <td>Message</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%x</tt></td>
 *     <td>Cause</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%n</tt></td>
 *     <td>Newline</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%T</tt></td>
 *     <td>Qualified class name of the caller issuing the logging request</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%M</tt></td>
 *     <td>The method name where the logging request was issued</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%F</tt></td>
 *     <td>The file name where the logging request was issued</td>
 *   </tr>
 *
 *   <tr>
 *     <td><tt>%L</tt></td>
 *     <td>The line number from where the logging request was issued</td>
 *   </tr>
 * </table>
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class PatternRenderer
    implements Renderer
{
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * @since 1.6
     */
    public static final String DEFAULT_PATTERN = "[%l] %c - %m%n%x";

    private String pattern;

    public PatternRenderer() {
        this(DEFAULT_PATTERN);
    }

    /**
     * @since 1.6
     */
    public PatternRenderer(final String pattern) {
        setPattern(pattern);
    }

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
        if (pattern == null) {
            throw new NullPointerException();
        }
        this.pattern = pattern;
    }

    public String render(final Event event) {
        if (event == null) {
            throw new NullPointerException();
        }

        final String _pattern = getPattern();
        final StringBuilder buff = new StringBuilder();

        int len = _pattern.length();
        for (int i=0; i < len; i++) {
            char c = _pattern.charAt(i);
            if (c == '%') {
                if (i + 1 >= len) {
                    throw new IllegalArgumentException("Invalid pattern: " + _pattern);
                }
                char t = _pattern.charAt(++i);

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
                        renderTraceClass(event, buff);
                        break;

                    case 'F':
                        renderTraceFile(event, buff);
                        break;

                    case 'M':
                        renderTraceMethod(event, buff);
                        break;

                    case 'L':
                        renderTraceLine(event, buff);
                        break;

                    case 'm':
                        renderMessage(event, buff);
                        break;

                    case 'x':
                        renderCause(event, buff);
                        break;

                    // TODO: Maybe try and add MDC support here?  Probably %X{foo}

                    case 'n':
                        renderNewLine(buff);
                        break;

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
        if (name != null) {
            if (shortName) {
                int i = name.lastIndexOf(".");

                if (i != -1) {
                    name = name.substring(i + 1, name.length());
                }
            }

            buff.append(name);
        }
        else {
            buff.append("<anonymous>");
        }
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
        if (cause != null) {
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
                    buff.append("Caused by: ").append(cause.getClass().getName()).append(": ");
                    buff.append(cause.getMessage());
                    buff.append(NEWLINE);
                }
            }
        }
    }

    protected void renderTraceFile(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        StackTraceElement[] trace = event.getTrace();
        if (trace != null) {
            buff.append(trace[0].getFileName());
        }
    }

    protected void renderTraceClass(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        StackTraceElement[] trace = event.getTrace();
        if (trace != null) {
            buff.append(trace[0].getClassName());
        }
    }

    protected void renderTraceMethod(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        StackTraceElement[] trace = event.getTrace();
        if (trace != null) {
            buff.append(trace[0].getMethodName());
        }
    }

    protected void renderTraceLine(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        StackTraceElement[] trace = event.getTrace();
        if (trace != null) {
            buff.append(trace[0].getLineNumber());
        }
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