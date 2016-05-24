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
package com.planet57.gossip.source;

import com.planet57.gossip.model.Model;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Tests for the {@link URLSource} class.
 */
public class URLSourceTest
{
  @Test
  public void test1() throws Exception {
    URLSource s = new URLSource();

    URL url = getClass().getResource("config1.properties");
    assertNotNull(url);
    s.setUrl(url);

    Model model = s.load();
    assertNotNull(model);
  }
}