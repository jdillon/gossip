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

import com.planet57.gossip.Level;

/**
 * Logger node.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class LoggerNode
    extends Node
    implements Serializable
{
  private String name;

  private String level;

  public String getLevel() {
    return level;
  }

  public String getName() {
    return name;
  }

  public void setLevel(final String level) {
    this.level = level;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Level asLevel() {
    return Level.valueOf(getLevel().toUpperCase());
  }

  public String toString() {
    return getClass().getSimpleName() +
        "{name=" + getName() +
        ",level=" + getLevel() +
        "}";
  }

  public String getId() {
    String id = super.getId();
    if (id == null) {
      return getName();
    }
    return id;
  }
}
