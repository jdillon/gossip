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
package com.planet57.gossip.extra.render;


import com.planet57.gossip.Event;
import org.junit.Test;
import org.slf4j.Logger;
import com.planet57.gossip.Gossip;
import com.planet57.gossip.Level;

/**
 * Tests for {@link ColorRenderer}.
 */
public class ColorRendererTest
{
  @Test
  public void test1() throws Exception {
    Logger log = Gossip.getInstance().getLogger("foo");
    Event e = new Event(log, Level.DEBUG, "foo bar baz", null);
    ColorRenderer r = new ColorRenderer();
    String text = r.render(e);
    System.out.println(text);
  }
}