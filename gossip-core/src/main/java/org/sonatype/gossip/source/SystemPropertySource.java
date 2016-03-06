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
package org.sonatype.gossip.source;

import org.sonatype.gossip.ConfigurationException;
import org.sonatype.gossip.MissingPropertyException;
import org.sonatype.gossip.model.Model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * System-property configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class SystemPropertySource
    extends SourceSupport
{
    public SystemPropertySource() {}

    public SystemPropertySource(final String name) {
        setName(name);
    }

    private String name;

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Model load() throws Exception {
        if (name == null) {
            throw new MissingPropertyException("name");
        }

        String value = System.getProperty(name);

        if (value == null) {
            log.trace("Unable to load; property not set: {}", name);
            return null;
        }

        URL url = null;
        try {
            url = new URL(value);
        }
        catch (MalformedURLException e) {
            File file = new File(value);

            if (file.exists()) {
                url = file.toURI().toURL();
            }
        }

        if (url == null) {
            throw new ConfigurationException("Unable to load; unable to resolve target: " + value);
        }

        return load(url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}