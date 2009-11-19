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

import org.sonatype.gossip.MissingPropertyException;
import org.sonatype.gossip.model.Model;

import java.io.File;

/**
 * Home-directory configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class HomeDirectorySource
    extends SourceSupport
{
    private String path;

    public void setPath(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Model load() throws Exception {
        if (path == null) {
            throw new MissingPropertyException("path");
        }

        File homeDir = new File(System.getProperty("user.home"));

        File file = new File(homeDir, path);

        return load(file);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "path='" + path + '\'' +
                '}';
    }
}