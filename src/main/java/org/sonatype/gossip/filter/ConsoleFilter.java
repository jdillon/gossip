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

package org.sonatype.gossip.filter;

import org.sonatype.gossip.ConfigurationException;
import org.sonatype.gossip.Event;
import org.sonatype.gossip.filter.render.Renderer;
import org.sonatype.gossip.filter.render.SimpleRenderer;

import java.io.PrintStream;

/**
 * Writes events to console.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ConsoleFilter
    implements Filter
{
    public static final String SYSOUT = "SYSOUT";

    public static final String SYSTEM_OUT = "SYSTEM.OUT";

    public static final String SYSERR = "SYSERR";

    public static final String SYSTEM_ERR = "SYSTEM.ERR";

    private String name = SYSTEM_OUT;

    private PrintStream stream;

    private Renderer renderer;

    public ConsoleFilter() {
        this(SYSOUT);
    }

    public ConsoleFilter(final String name) {
        setName(name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{name='" + name + '\'' +
                ",renderer=" + renderer +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        assert name != null;

        this.name = name;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    private PrintStream getStream() {
        if (stream == null) {
            // Parse the stream
            String tmp = name.toUpperCase();
            if (SYSOUT.equals(tmp) || SYSTEM_OUT.equals(tmp)) {
                stream = System.out;
            }
            else if (SYSERR.equals(tmp) || SYSTEM_ERR.equals(tmp)) {
                stream = System.err;
            }
            else {
                throw new ConfigurationException("Unknown stream name: " + name);
            }
        }

        return stream;
    }

    public Event filter(final Event event) {
        assert event != null;

        if (renderer == null) {
            renderer = new SimpleRenderer();
        }
        
        String text = renderer.render(event);

        final PrintStream out = getStream();
        
        synchronized (out) {
            out.print(text);
            out.flush();
        }

        return STOP;
    }
}