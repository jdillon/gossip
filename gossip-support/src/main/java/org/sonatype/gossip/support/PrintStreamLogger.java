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
import org.sonatype.gossip.Event;
import org.sonatype.gossip.Level;
import org.sonatype.gossip.LoggerSupport;
import org.sonatype.gossip.render.PatternRenderer;
import org.sonatype.gossip.render.Renderer;

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

    private volatile Level threshold;

    private volatile Renderer renderer;

    public PrintStreamLogger(final PrintStream stream, final Level threshold) {
        if (stream == null) {
            throw new NullPointerException();
        }
        // threshold can be null
        this.stream = stream;
        this.threshold = threshold;
        setRenderer(createRenderer());
    }

    public PrintStreamLogger(final PrintStream stream) {
        this(stream, null);
    }

    /**
     * Returns a default {@link PatternRenderer}.
     */
    protected Renderer createRenderer() {
        return new PatternRenderer();
    }

    public PrintStream getStream() {
        return stream;
    }

    public Level getThreshold() {
        if (threshold == null) {
            return Level.TRACE;
        }
        return threshold;
    }

    public void setThreshold(final Level threshold) {
        // null will reset to default
        this.threshold = threshold;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final Renderer renderer) {
        if (renderer == null) {
            throw new NullPointerException();
        }
        this.renderer = renderer;
    }

    public void setName(final String name) {
        // null will reset to default
        this.name = name;
    }

    protected boolean isEnabled(final Level level) {
        assert level != null;
        return getThreshold().id <= level.id;
    }

    @Override
    protected void doLog(final Event event) {
        assert event != null;

        final PrintStream out = getStream();
        synchronized (out) {
            out.print(getRenderer().render(event));
            out.flush();
        }
    }
}