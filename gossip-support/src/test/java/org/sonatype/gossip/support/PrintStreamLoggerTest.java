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

package org.sonatype.gossip.support;

import org.junit.Test;
import org.sonatype.gossip.Level;

/**
 * Tests for {@link PrintStreamLogger}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.1
 */
public class PrintStreamLoggerTest
{
    // Not really tests, more like helpers for manual verification...

    @Test
    public void testSimple() {
        PrintStreamLogger log = new PrintStreamLogger(System.out);
        System.out.println("LOG: " + log);

        log.info("Test");
    }

    @Test
    public void testWithName() {
        PrintStreamLogger log = new PrintStreamLogger(System.out);
        log.setName("foo");
        System.out.println("LOG: " + log);

        log.info("Test");
    }

    @Test
    public void testWithLevel() {
        PrintStreamLogger log = new PrintStreamLogger(System.out);
        log.setLevel(Level.WARN);
        System.out.println("LOG: " + log);

        log.info("Test");
        log.error("Test");
    }

    @Test
    public void testWithCause() {
        PrintStreamLogger log = new PrintStreamLogger(System.out);
        log.error("oops", new Throwable());
    }
}
