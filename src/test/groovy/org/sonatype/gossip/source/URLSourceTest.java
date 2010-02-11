/*
 * Copyright (C) 2009 the original author or authors.
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
import org.sonatype.gossip.model.Model;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Tests for the {@link org.sonatype.gossip.source.URLSource} class.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
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