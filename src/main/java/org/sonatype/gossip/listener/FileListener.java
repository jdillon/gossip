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

package org.sonatype.gossip.listener;

import org.sonatype.gossip.Event;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Writes events to a file.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class FileListener
    extends ListenerSupport
{
    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    
    private File file;

    private boolean append;

    private int bufferSize = 8192;

    private RollingStrategy rollingStrategy;

    private CountingWriter writer;

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setFile(final String fileName) {
        assert fileName != null;
        setFile(new File(evaluate(fileName.trim())));
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(final boolean append) {
        this.append = append;
    }

    public void setAppend(final String append) {
        setAppend(Boolean.parseBoolean(append));
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setBufferSize(final String bufferSize) {
        setBufferSize(Integer.parseInt(bufferSize));
    }

    public RollingStrategy getRollingStrategy() {
        return rollingStrategy;
    }

    public void setRollingStrategy(final RollingStrategy rollingStrategy) {
        this.rollingStrategy = rollingStrategy;
    }

    public CountingWriter getWriter() {
        return writer;
    }

    protected CountingWriter createWriter() throws IOException {
        File file = getFile();
        File dir = file.getParentFile();
        if (dir != null && !dir.mkdirs()) {
            log.warn("Unable to create directory structure for: {}", file);
        }

        log.trace("Creating writer for file: {}", file);
        Writer writer = new FileWriter(file, isAppend());

        // Maybe buffer
        int n = getBufferSize();
        if (n > 0) {
            log.trace("Using buffer size: {}", n);
            writer = new BufferedWriter(writer, n);
        }

        return new CountingWriter(writer);
    }

    public void onEvent(final Event event) throws Exception {
        assert event != null;

        if (writer == null) {
            writer = createWriter();
        }
        else {
            // Maybe roll the file
            RollingStrategy roller = getRollingStrategy();
            if (roller != null && roller.roll(this)) {
                // Rebuild the writer after a roll
                writer = createWriter();
            }
        }

        synchronized (writer) {
            writer.write(render(event));
            writer.flush();
        }
    }

    protected String evaluate(String input) {
        if (input != null) {
            Matcher matcher = PATTERN.matcher(input);

            while (matcher.find()) {
                Object rep = getProperty(matcher.group(1));
                if (rep != null) {
                    input = input.replace(matcher.group(0), rep.toString());
                    matcher.reset(input);
                }
            }
        }

        return input;
    }

    protected Object getProperty(final String name) {
        assert name != null;
        return System.getProperty(name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "file=" + file +
            ", append=" + append +
            ", rollingStrategy=" + rollingStrategy +
            '}';
    }

    /**
     * Interface for file rolling strategy's.
     */
    public static interface RollingStrategy
    {
        boolean roll(FileListener listener);
    }

    //
    // TODO: Add some rolling strategies
    //
}