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

package com.planet57.gossip.model;

import java.io.Serializable;

/**
 * Factory node.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public abstract class FactoryNode
    extends Node
    implements Serializable
{
  private String type;

  private Object configuration;

  public Object getConfiguration() {
    return configuration;
  }

  public String getType() {
    return type;
  }

  public void setConfiguration(final Object configuration) {
    this.configuration = configuration;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String toString() {
    return getClass().getSimpleName() +
        "{type=" + getType() +
        ",configuration=" + getConfiguration() +
        " }";
  }

  public void setType(final Class type) {
    setType(type.getName());
  }

  public String getId() {
    String id = super.getId();
    if (id == null) {
      return getType();
    }
    return id;
  }
}
