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

package org.slf4j.gossip;

import junit.framework.TestCase;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

/**
 * Tests for the {@link Gossip} class.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GossipTest
    extends TestCase
{
    public void testBasic() throws Exception {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();

        assertNotNull(factory);
        assertEquals(Gossip.class.getName(), factory.getClass().getName());
        
        org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
        assertNotNull(log);

        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}