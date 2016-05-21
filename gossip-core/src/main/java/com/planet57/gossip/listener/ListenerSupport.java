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
import org.slf4j.Logger;
import com.planet57.gossip.Level;
import com.planet57.gossip.Log;
import com.planet57.gossip.render.PatternRenderer;
import com.planet57.gossip.render.Renderer;

/**
 * Support for {@link Listener} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public abstract class ListenerSupport
    implements Listener
{
  protected final Logger log = Log.getLogger(getClass());

  private Renderer renderer;

  private Level threshold = Level.TRACE;

  public Renderer getRenderer() {
    return renderer;
  }

  protected Renderer createRenderer() {
    return new PatternRenderer();
  }

  public void setRenderer(final Renderer renderer) {
    this.renderer = renderer;
  }

  public Level getThreshold() {
    return threshold;
  }

  public void setThreshold(final Level threshold) {
    this.threshold = threshold;
  }

  public void setThreshold(final String threshold) {
    assert threshold != null;
    setThreshold(Level.valueOf(threshold));
  }

  /**
   * Returns false if the given event does not match the threshold.
   *
   * @param event The event to evaluate.
   * @return True if the event is loggable.
   */
  protected final boolean isLoggable(final Event event) {
    assert event != null;
    return threshold == null || event.getLevel().id >= threshold.id;
  }

  protected String render(final Event event) {
    assert event != null;

    Renderer renderer = getRenderer();
    if (renderer == null) {
      renderer = createRenderer();
      setRenderer(renderer);
    }

    return renderer.render(event);
  }
}