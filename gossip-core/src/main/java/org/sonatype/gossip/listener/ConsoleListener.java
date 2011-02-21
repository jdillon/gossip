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

package org.sonatype.gossip.listener;

import org.sonatype.gossip.Event;

import java.io.PrintStream;

/**
 * Writes events to console.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ConsoleListener
    extends ListenerSupport
{
    public static enum Stream
    {
        OUT, ERR
    }

    private Stream stream;

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

    public void onEvent(final Event event) {
        assert event != null;

        if (!isLoggable(event)) return;

        Stream stream = getStream();
        PrintStream out;

        switch (stream) {
            case OUT:
                out = System.out;
                break;
            case ERR:
                out = System.err;
                break;
            default:
                throw new InternalError();
        }

        out.print(render(event));
        out.flush();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "stream=" + stream +
            ", threshold=" + getThreshold() +
            '}';
    }
}