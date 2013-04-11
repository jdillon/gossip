/*
 * Copyright (c) 2009-2013 the original author or authors.
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
package org.sonatype.gossip.source;

import org.junit.Test;
import org.sonatype.gossip.ConfigurationException;
import org.sonatype.gossip.model.Model;

import static org.junit.Assert.*;

/**
 * Tests for the {@link org.sonatype.gossip.source.SystemPropertySource} class.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class SystemPropertySourceTest
{
    @Test
    public void test1() throws Exception {
        SystemPropertySource s = new SystemPropertySource();
        String name = "foo.bar";
        s.setName(name);

        System.setProperty(name, "no such file anywhere I hope");
        try {
            s.load();
            fail();
        }
        catch (ConfigurationException e) {
            // expected
        }
    }

    @Test
    public void test2() throws Exception {
        SystemPropertySource s = new SystemPropertySource();
        String name = "foo.bar";
        s.setName(name);

        System.getProperties().remove(name);

        Model model = s.load();
        assertNull(model);
    }

    @Test
    public void test3() throws Exception {
        try {
            SystemPropertySource s = new SystemPropertySource();
            s.load();
            fail();
        }
        catch (ConfigurationException expected) {}
    }
}