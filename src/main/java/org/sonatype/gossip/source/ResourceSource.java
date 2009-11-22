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

import java.net.URL;

import static org.sonatype.gossip.source.ResourceSource.ClassLoaderType.TCL;

/**
 * Resource-based configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ResourceSource
    extends SourceSupport
{
    public static enum ClassLoaderType {
        TCL, INTERNAL, SYSTEM;
    }
    
    private String name;

    private ClassLoaderType classLoaderType = TCL;

    private ClassLoader classLoader;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setClassLoaderType(final String type) {
        assert type != null;
        
        this.classLoaderType = ClassLoaderType.valueOf(type.toUpperCase());
    }

    public ClassLoaderType getClassLoaderType() {
        return classLoaderType;
    }

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            switch (classLoaderType) {
                case TCL:
                    classLoader = Thread.currentThread().getContextClassLoader();
                    break;
                case INTERNAL:
                    classLoader = getClass().getClassLoader();
                    break;
                case SYSTEM:
                    classLoader = ClassLoader.getSystemClassLoader();
                    break;
            }
        }

        return classLoader;
    }

    public void setClassLoader(final ClassLoader cl) {
        this.classLoader = cl;
    }

    public Model load() throws Exception {
        if (name == null) {
            throw new MissingPropertyException("name");
        }

        Model model = null;

        ClassLoader cl = getClassLoader();
        assert cl != null;
        
        log.trace("Loading resource for name: {}, CL: {}", name, cl);
        
        URL url = cl.getResource(name);

        if (url == null) {
            log.trace("Unable to load; missing resource: {}", name);
        }
        else {
            log.trace("Loaded resource: {}", url);
            model = load(url);
        }

        return model;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", classLoaderType='" + classLoaderType + '\'' +
                ", classLoader=" + classLoader +
                '}';
    }
}