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
package com.planet57.gossip.render;

import com.planet57.gossip.Event;
import com.planet57.gossip.Level;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class PatternRendererTest {

    @Test
    public void renderRelative() throws Exception {
        final long time = getTime("%r [%l] %m%n%x");
        assertThat(time, both(greaterThanOrEqualTo(0L)).and(lessThan(10000L)));
    }

    @Test
    public void renderAbsolute() throws Exception {
        final long currentTimeMillis = System.currentTimeMillis();
        final long time = getTime("%d [%l] %m%n%x");
        assertThat(time, greaterThanOrEqualTo(currentTimeMillis));
    }

    private long getTime(String pattern) {
        final PatternRenderer sut = new PatternRenderer(pattern);
        final String result = sut.render(new Event(null, Level.INFO, "Hello", null));
        assertThat(result, endsWith("[INFO] Hello\n"));
        final String[] split = result.split(" ");
        return Long.valueOf(split[0]);
    }
}