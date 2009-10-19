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

package org.sonatype.gossip.model.source;

import org.sonatype.gossip.MissingPropertyException;
import org.sonatype.gossip.model.Model;

import java.io.File;

/**
 * Local file configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class FileSource
    extends SourceSupport
{
    private File file;

    public FileSource() {}

    public FileSource(final File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setFile(final String fileName) {
        assert fileName != null;
        
        setFile(new File(fileName));
    }

    public Model load() throws Exception {
        if (file == null) {
            throw new MissingPropertyException("file");
        }

        return load(getFile());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "file=" + file +
                '}';
    }
}