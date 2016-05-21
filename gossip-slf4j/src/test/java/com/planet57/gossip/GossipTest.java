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
package com.planet57.gossip;

import org.junit.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link Gossip} class.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GossipTest
{
  @Test
  public void testBasic() throws Exception {
    ILoggerFactory factory = LoggerFactory.getILoggerFactory();

    assertNotNull(factory);
    assertEquals(Gossip.class.getName(), factory.getClass().getName());

    Logger log = LoggerFactory.getLogger(getClass());
    assertNotNull(log);

    log.trace("trace");
    log.debug("debug");
    log.info("info");
    log.warn("warn");
    log.error("error");
  }
}