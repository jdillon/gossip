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

import com.planet57.gossip.Log;
import com.planet57.gossip.listener.CountingWriter;
import com.planet57.gossip.listener.FileListener;
import com.planet57.gossip.listener.FileListener.RollingStrategy;
import org.slf4j.Logger;

import java.io.File;

/**
 * A file-size {@link RollingStrategy}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public class FileSizeRollingStrategy
    implements RollingStrategy
{
  private static final Logger log = Log.getLogger(FileSizeRollingStrategy.class);

  private long maximumFileSize = 10 * 1024 * 1024;

  private int maximumBackupIndex = 1;

  public int getMaximumBackupIndex() {
    return maximumBackupIndex;
  }

  public void setMaximumBackupIndex(final int n) {
    this.maximumBackupIndex = n;
  }

  public long getMaximumFileSize() {
    return maximumFileSize;
  }

  public void setMaximumFileSize(final long n) {
    this.maximumFileSize = n;
  }

  public boolean roll(final FileListener listener) {
    assert listener != null;

    CountingWriter writer = listener.getWriter();
    if (writer.size() > maximumFileSize) {
      return false;
    }

    //
    // This was copied from Log4j's RollingFileAppender and massaged a bit, probably needs more work on Windows
    //

    File source = listener.getFile();
    File target;
    boolean renameSucceeded = true;

    // If maxBackups <= 0, then there is no file renaming to be done.
    if (maximumBackupIndex > 0) {
      // Delete the oldest file, to keep Windows happy.
      File file = new File(source.getPath() + '.' + maximumBackupIndex);
      if (file.exists()) {
        renameSucceeded = file.delete();
      }

      // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
      for (int i = maximumBackupIndex - 1; i >= 1 && renameSucceeded; i--) {
        file = new File(source.getPath() + "." + i);
        if (file.exists()) {
          target = new File(source.getPath() + '.' + (i + 1));
          log.debug("Renaming file {} to {}", file, target);
          renameSucceeded = file.renameTo(target);
        }
      }

      if (renameSucceeded) {
        // Rename fileName to fileName.1
        target = new File(source.getPath() + "." + 1);
        file = new File(source.getPath());
        log.debug("Renaming file {} to {}", file, target);
        renameSucceeded = file.renameTo(target);

        if (!renameSucceeded) {
          return false;
        }
      }
    }

    return true;
  }
}