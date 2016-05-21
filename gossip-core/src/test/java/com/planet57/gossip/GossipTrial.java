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
import org.slf4j.Logger;

/**
 * Created by jason on 5/20/16.
 */
public class GossipTrial
{
    @Test
    public void test() throws Exception {
        Gossip.getInstance().getRoot().setLevel(Level.INFO);
        Logger log1 = Gossip.getInstance().getLogger("log1");
        log1.debug("test1");
        log1.info("test1");
        log1.warn("test1");
        log1.error("test1");

        System.out.println("\n\nHERE\n\n");

        Gossip.getInstance().getRoot().setLevel(Level.ERROR);
        Logger log2 = Gossip.getInstance().getLogger("log2");

        log1.debug("test1");
        log1.info("test1");
        log1.warn("test1");
        log1.error("test1");

        log2.debug("test2");
        log2.info("test2");
        log2.warn("test2");
        log2.error("test2");
    }
}
