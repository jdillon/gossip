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

package org.slf4j.gossip.config;

import junit.framework.TestCase;
import org.slf4j.gossip.Event;
import org.slf4j.gossip.Level;
import org.slf4j.gossip.model.render.ColorRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for the {@link ColorRenderer} class.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ColorRendererTest
    extends TestCase
{
    public void test1() throws Exception {
        Logger log = LoggerFactory.getLogger("foo");
        Event e = new Event(log, Level.DEBUG, "foo bar baz", null);

        ColorRenderer r = new ColorRenderer();
        r.render(e);
    }
}