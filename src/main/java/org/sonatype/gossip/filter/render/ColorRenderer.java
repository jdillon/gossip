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

import jline.Terminal;
import jline.TerminalFactory;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.sonatype.gossip.Event;

/**
 * Renders events with ANSI colors.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ColorRenderer
    implements Renderer
{
    //
    // FIXME: SHould be able o make this into a filter, need to update the filter api to allow for mutation of the values
    //
    
    private boolean truncate = false;

    private int maxLength;

    public ColorRenderer() {
        Terminal term = TerminalFactory.get();
        int w = term.getWidth() - 1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "truncate=" + truncate +
                ", maxLength=" + maxLength +
                '}';
    }

    public void setTruncate(final boolean flag) {
        this.truncate = flag;
    }

    public void setTruncate(final String flag) {
        setTruncate(Boolean.valueOf(flag).booleanValue());
    }

    public boolean isTruncate() {
        return truncate;
    }

    public void setMaxLength(final int length) {
        this.maxLength = length;
    }

    public void setMaxLength(final String length) {
        assert length != null;

        setMaxLength(Integer.parseInt(length));
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String render(final Event event) {
        assert event != null;

        Ansi ansi = Ansi.ansi();

        ansi = ansi.a("[");

        switch (event.level) {
            case TRACE:
            case DEBUG:
                ansi = ansi.fg(Color.YELLOW).a(event.level.name()).reset();
                break;

            case INFO:
                ansi = ansi.fg(Color.GREEN).a(event.level.name()).reset();
                break;

            case WARN:
            case ERROR:
                ansi = ansi.fg(Color.RED).a(event.level.name()).reset();
                break;

            default:
                throw new InternalError();
        }

        ansi = ansi.a("]");

        switch (event.level) {
            case INFO:
            case WARN:
                ansi = ansi.a(" ");
        }

        ansi = ansi.a(" ").a(event.logger.getName()).a(" - ").a(event.message).a(NEWLINE);

        if (event.cause != null) {
            ansi = ansi.a(event.toString());

            StackTraceElement[] trace = event.cause.getStackTrace();
            for (int i=0; i<trace.length; i++ ) {
                ansi = ansi.a(trace[i].toString());
            }
        }

        //
        // FIXME: Neeed a better solution for this which handles exceptions, multi-line messages
        //        and color escaping properly...
        //

        // if (truncate && buff.getPlainBuffer().length() > maxLength) {
        //     return buff.toString().substring(0, maxLength - 4) + " ..." + NEWLINE;
        // }

        return ansi.toString();
    }
}