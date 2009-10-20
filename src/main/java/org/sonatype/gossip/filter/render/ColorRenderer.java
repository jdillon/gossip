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

package org.sonatype.gossip.filter.render;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.Attribute.*;
import org.sonatype.gossip.Event;

/**
 * Renders events with ANSI colors.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ColorRenderer
    extends SimpleRenderer
{
    @Override
    protected void appendLevel(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;
        
        Ansi ansi = ansi();

        switch (event.level) {
            case TRACE:
            case DEBUG:
                ansi = ansi.a(INTENSITY_BOLD).fg(YELLOW).a(event.level.name()).reset();
                break;

            case INFO:
                ansi = ansi.a(INTENSITY_BOLD).fg(GREEN).a(event.level.name()).reset();
                break;

            case WARN:
            case ERROR:
                ansi = ansi.a(INTENSITY_BOLD).fg(RED).a(event.level.name()).reset();
                break;

            default:
                throw new InternalError();
        }
        
        buff.append(ansi);
    }
    
    @Override
    protected void appendCause(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        Throwable cause = event.cause;

        buff.append(ansi().a(INTENSITY_BOLD).fg(RED).a(cause.getClass().getName()).reset());
        buff.append(": ");
        buff.append(ansi().a(INTENSITY_BOLD).fg(RED).a(cause.getMessage()).reset());
        buff.append(NEWLINE);

        while (cause != null) {
            for (StackTraceElement e : cause.getStackTrace()) {
                buff.append("    ");
                buff.append(ansi().a(INTENSITY_BOLD).a("at").reset().a(" ").
                    a(e.getClassName()).a(".").a(e.getMethodName()).
                    a(" (").a(INTENSITY_BOLD).a(getLocation(e)).reset().a(")"));
                buff.append(NEWLINE);
            }

            cause = cause.getCause();
            if (cause != null) {
                buff.append(ansi().a(INTENSITY_BOLD).a("Caused by").reset().a(" ").a(cause.getClass().getName()).a(": "));
                buff.append(ansi().a(INTENSITY_BOLD).fg(RED).a(cause.getMessage()).reset());
                buff.append(NEWLINE);
            }
        }
    }
}