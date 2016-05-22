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
package com.planet57.gossip.extra.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.planet57.gossip.Event;
import com.planet57.gossip.Gossip;
import com.planet57.gossip.Level;
import com.planet57.gossip.listener.CountingWriter;
import com.planet57.gossip.listener.FileListener;
import com.planet57.gossip.render.PatternRenderer;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FileSizeRollingStrategy}.
 */
public class FileSizeRollingStrategyTest
{
  private static final int NEWLINE_LENGTH = System.getProperty("line.separator").length();

  private File getBaseDir() {
    File dir;

    // If ${basedir} is set, then honor it
    String tmp = System.getProperty("basedir");
    if (tmp != null) {
      dir = new File(tmp);
    }
    else {
      // Find the directory which this class (or really the sub-class of TestSupport) is defined in.
      String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

      // We expect the file to be in target/test-classes, so go up 2 dirs
      dir = new File(path).getParentFile().getParentFile();

      // Set ${basedir} which is needed by logging to initialize
      System.setProperty("basedir", dir.getPath());
    }

    return dir;
  }

  private FileListener createListener(final String name) {
    FileListener listener = new FileListener();

    PatternRenderer renderer = new PatternRenderer();
    listener.setRenderer(renderer);

    File targetDir = new File(getBaseDir(), "target");
    File file = new File(targetDir, name);
    file.delete();
    listener.setFile(file);

    FileSizeRollingStrategy strategy = new FileSizeRollingStrategy();
    strategy.setMaximumFileSize(30);
    strategy.setMaximumBackupIndex(2);
    listener.setRollingStrategy(strategy);
    return listener;
  }

  @SuppressWarnings("Since15")
  private String readFile(final File file) throws IOException {
    byte[] bytes = Files.readAllBytes(file.toPath());
    return new String(bytes, "UTF-8");
  }

  @Test
  public void test1() throws Exception {
    FileListener listener = createListener("test1.log");

    Logger logger = Gossip.getInstance().getLogger("a");
    String msg = "1234567890";
    Event event = new Event(logger, Level.INFO, msg, null);

    listener.onEvent(event);

    CountingWriter writer = listener.getWriter();
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());
  }

  @Test
  public void test2() throws Exception {
    FileListener listener = createListener("test2.log");

    Logger logger = Gossip.getInstance().getLogger("a");
    String msg = "1234567890";
    Event event = new Event(logger, Level.INFO, msg, null);

    listener.onEvent(event);

    CountingWriter writer = listener.getWriter();
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    listener.onEvent(event);
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    File targetDir = new File(getBaseDir(), "target");
    File rolled = new File(targetDir, "test2.log.1");
    assertEquals(11 + 10 + NEWLINE_LENGTH, readFile(listener.getFile()).length());
  }

  @Test
  public void test3() throws Exception {
    FileListener listener = createListener("test3.log");

    Logger logger = Gossip.getInstance().getLogger("a");
    String msg = "1234567890";
    Event event = new Event(logger, Level.INFO, msg, null);

    listener.onEvent(event);

    CountingWriter writer = listener.getWriter();
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    listener.onEvent(event);
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    listener.onEvent(event);
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    listener.onEvent(event);
    assertEquals(11 + 10 + NEWLINE_LENGTH, writer.size());

    File targetDir = new File(getBaseDir(), "target");
    assertEquals(11 + 10 + NEWLINE_LENGTH, readFile(new File(targetDir, "test3.log")).length());
    assertEquals(11 + 10 + NEWLINE_LENGTH, readFile(new File(targetDir, "test3.log.1")).length());
    assertEquals(11 + 10 + NEWLINE_LENGTH, readFile(new File(targetDir, "test3.log.2")).length());
  }
}