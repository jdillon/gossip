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
package com.planet57.gossip.model.io.props;

import com.planet57.gossip.model.Model;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Tests for {@link GossipPropertiesReader}.
 */
public class GossipPropertiesReaderTest
{
  @Test
  public void testLoad() throws IOException {
    URL url = getClass().getResource("gossip1.properties");
    GossipPropertiesReader reader = new GossipPropertiesReader();
    Model model = reader.read(url);
    System.out.println(model);
  }
}