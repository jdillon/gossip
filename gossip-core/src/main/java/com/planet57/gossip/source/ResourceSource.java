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
package com.planet57.gossip.source;

import com.planet57.gossip.MissingPropertyException;
import com.planet57.gossip.model.Model;

import java.net.URL;

/**
 * Resource-based configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ResourceSource
    extends SourceSupport
{
  public static enum ClassLoaderType
  {
    TCL, INTERNAL, SYSTEM;
  }

  private String name;

  private ClassLoaderType classLoaderType = ClassLoaderType.TCL;

  private ClassLoader classLoader;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setClassLoaderType(final ClassLoaderType type) {
    assert type != null;
    this.classLoaderType = type;
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

    log.trace("Loading resource for name: {}, CL: {} ({})", new Object[]{name, cl, classLoaderType});

    URL url = cl.getResource(name);

    // HACK: This is needed as a fallback on Maven 2.0.x and 2.2.x which does not have the TCL setup as expected
    if (url == null) {
      cl = getClass().getClassLoader();
      log.trace("Configured CL failed, trying {}: {}", ClassLoaderType.INTERNAL, cl);
      url = cl.getResource(name);
    }

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