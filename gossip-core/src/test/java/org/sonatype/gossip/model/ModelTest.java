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
package org.sonatype.gossip.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Model}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ModelTest
{
    @Test
    public void testLoggerNodeIdentity() {
        LoggerNode node1 = new LoggerNode();
        node1.setName("foo");

        LoggerNode node2 = new LoggerNode();
        node2.setName("bar");

        LoggerNode node3 = new LoggerNode();
        node3.setName("foo");

        List<LoggerNode> list = new ArrayList<LoggerNode>();
        list.add(node1);

        assertTrue(list.contains(node1));
        assertFalse(list.contains(node2));
        assertTrue(list.contains(node3));
    }

    @Test
    public void testListenerNodeIdenityer() {
        ListenerNode node1 = new ListenerNode();
        node1.setType("foo");

        ListenerNode node2 = new ListenerNode();
        node2.setType("bar");

        ListenerNode node3 = new ListenerNode();
        node3.setType("foo");

        List<ListenerNode> list = new ArrayList<ListenerNode>();
        list.add(node1);

        assertTrue(list.contains(node1));
        assertFalse(list.contains(node2));
        assertTrue(list.contains(node3));
    }
}