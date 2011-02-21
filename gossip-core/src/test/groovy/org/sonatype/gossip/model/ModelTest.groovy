/**
 * Copyright (c) 2009-2011 the original author or authors.
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
package org.sonatype.gossip.model

import org.junit.Test
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Tests for {@link Model}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class ModelTest
{
    @Test
    void test1() {
        def node1 = new LoggerNode()
        node1.name = 'foo'

        def node2 = new LoggerNode()
        node2.name = 'bar'

        def node3 = new LoggerNode()
        node3.name = 'foo'

        def list = []
        list << node1

        assertTrue(list.contains(node1))
        assertFalse(list.contains(node2))
        assertTrue(list.contains(node3))
    }

    @Test
    void test2() {
        def node1 = new ListenerNode()
        node1.type = 'foo'

        def node2 = new ListenerNode()
        node2.type = 'bar'

        def node3 = new ListenerNode()
        node3.type = 'foo'

        def list = []
        list << node1

        assertTrue(list.contains(node1))
        assertFalse(list.contains(node2))
        assertTrue(list.contains(node3))
    }
}