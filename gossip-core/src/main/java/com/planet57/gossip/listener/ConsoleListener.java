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
package com.planet57.gossip.listener;

import com.planet57.gossip.Event;

import java.io.PrintStream;

/**
 * Writes events to console.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ConsoleListener
    extends ListenerSupport
{
  public enum Stream
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

  protected PrintStream getOut() {
    switch (stream) {
      case OUT:
        return System.out;
      case ERR:
        return System.err;
      default:
        throw new InternalError();
    }
  }

  public void onEvent(final Event event) {
    assert event != null;

    if (!isLoggable(event)) {
      return;
    }

    String line = render(event);
    PrintStream out = getOut();
    synchronized (out) {
      out.print(line);
      out.flush();
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "stream=" + stream +
        ", threshold=" + getThreshold() +
        '}';
  }
}