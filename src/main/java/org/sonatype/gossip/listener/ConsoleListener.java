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

package org.sonatype.gossip.listener;

import org.sonatype.gossip.Event;
import org.sonatype.gossip.render.Renderer;
import org.sonatype.gossip.render.SimpleRenderer;

import java.io.PrintStream;

/**
 * Writes events to console.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ConsoleListener
    implements Listener
{
    public static enum Stream
    {
        OUT, ERR;

        public PrintStream getOutput() {
            switch (this) {
                case OUT:
                    return System.out;
                case ERR:
                    return System.err;
            }

            throw new InternalError();
        }
    }

    private Stream stream;

    private Renderer renderer;
    
    public ConsoleListener() {
        setStream(Stream.OUT);
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(final Stream stream) {
        assert stream != null;
        this.stream = stream;
    }

    public void setStream(final String name) {
        assert name != null;
        setStream(Stream.valueOf(name.toUpperCase()));
    }

    public Renderer getRenderer() {
        if (renderer == null) {
            renderer = new SimpleRenderer();
        }
        return renderer;
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    public void onEvent(final Event event) {
        assert event != null;

        final String text = getRenderer().render(event);
        final PrintStream out = getStream().getOutput();

        synchronized (out) {
            out.print(text);
            out.flush();
        }

    }

    @Override
    public String toString() {
        return "ConsoleListener{" +
            "stream=" + stream +
            ", renderer=" + renderer +
            '}';
    }
}