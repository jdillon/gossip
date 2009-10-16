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

package org.slf4j.gossip.model;

import org.slf4j.gossip.Event;
import org.slf4j.gossip.model.render.Renderer;
import org.slf4j.gossip.model.render.SimpleRenderer;

/**
 * Filter node.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public abstract class Filter
    extends AbstractNode
{
    private Renderer renderer;

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    protected String render(final Event event) {
        assert event != null;

        if (renderer == null) {
            renderer = new SimpleRenderer();
        }

        return renderer.render(event);
    }

    public abstract Result filter(Event event);

    //
    // Result
    //

    public static final int CONTINUE_ID = 0;

    public static final Result CONTINUE = new Result("CONTINUE", CONTINUE_ID);

    public static final int STOP_ID = 1;

    public static final Result STOP = new Result("STOP", STOP_ID);

    public static final class Result
    {
        public final String name;

        public final int id;

        private Result(final String name, final int id) {
            this.name = name;

            this.id = id;
        }

        public String toString() {
            return name;
        }
    }
}