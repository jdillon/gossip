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

package org.sonatype.gossip.listener

import org.junit.Test
import static org.junit.Assert.*
import org.sonatype.gossip.Event
import org.sonatype.gossip.Level
import org.sonatype.gossip.Gossip
import org.sonatype.gossip.render.SimpleRenderer

/**
 * Tests for {@link FileSizeRollingStrategy}.
 *
 * @author <a href="mailto:jason@planet57.com'>Jason Dillon</a>
 */
class FileSizeRollingStrategyTest
{
    private File getBaseDir() {
        File dir

        // If ${basedir} is set, then honor it
        String tmp = System.getProperty('basedir')
        if (tmp != null) {
            dir = new File(tmp)
        }
        else {
            // Find the directory which this class (or really the sub-class of TestSupport) is defined in.
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()

            // We expect the file to be in target/test-classes, so go up 2 dirs
            dir = new File(path).getParentFile().getParentFile()

            // Set ${basedir} which is needed by logging to initialize
            System.setProperty('basedir', dir.getPath())
        }

        return dir
    }
    
    @Test
    void test1() {
        def listener = new FileListener()
        listener.setRenderer(new SimpleRenderer())
        
        def file = new File("${getBaseDir()}/target", 'test1.log')
        listener.file = file
        
        def strategy = new FileSizeRollingStrategy()
        strategy.maximumFileSize = 20
        strategy.maximumBackupIndex = 2
        listener.rollingStrategy = strategy
        
        def logger = new Gossip().getLogger('a')
        def msg = '1234567890'
        def event = new Event(logger, Level.INFO, msg, null)
        
        listener.onEvent(event)
        
        def writer = listener.getWriter()
        assertEquals(11 + 8, writer.size())
    }

    @Test
    void test2() {
        def listener = new FileListener()
        listener.setRenderer(new SimpleRenderer())

        def file = new File("${getBaseDir()}/target", 'test2.log')
        listener.file = file

        def strategy = new FileSizeRollingStrategy()
        strategy.maximumFileSize = 20
        strategy.maximumBackupIndex = 2
        listener.rollingStrategy = strategy

        def logger = new Gossip().getLogger('a')
        def msg = '1234567890'
        def event = new Event(logger, Level.INFO, msg, null)

        listener.onEvent(event)

        def writer = listener.getWriter()
        assertEquals(11 + 8, writer.size())

        listener.onEvent(event)
        assertEquals(11 + 8, writer.size())

        def rolled = new File("${getBaseDir()}/target", "test2.log.1")
        assertEquals(11 + 8, file.text.size())
    }

    @Test
    void test3() {
        def listener = new FileListener()
        listener.setRenderer(new SimpleRenderer())

        def file = new File("${getBaseDir()}/target", 'test3.log')
        listener.file = file

        def strategy = new FileSizeRollingStrategy()
        strategy.maximumFileSize = 20
        strategy.maximumBackupIndex = 2
        listener.rollingStrategy = strategy

        def logger = new Gossip().getLogger('a')
        def msg = '1234567890'
        def event = new Event(logger, Level.INFO, msg, null)

        listener.onEvent(event)

        def writer = listener.getWriter()
        assertEquals(11 + 8, writer.size())

        listener.onEvent(event)
        assertEquals(11 + 8, writer.size())

        listener.onEvent(event)
        assertEquals(11 + 8, writer.size())

        listener.onEvent(event)
        assertEquals(11 + 8, writer.size())

        assertEquals(11 + 8, new File("${getBaseDir()}/target", "test3.log").text.size())
        assertEquals(11 + 8, new File("${getBaseDir()}/target", "test3.log.1").text.size())
        assertEquals(11 + 8, new File("${getBaseDir()}/target", "test3.log.2").text.size())
    }
}