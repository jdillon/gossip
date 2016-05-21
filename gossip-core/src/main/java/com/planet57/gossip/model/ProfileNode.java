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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Profile node.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ProfileNode
    extends Node
    implements Serializable
{
  private String name;

  private Properties properties;

  private List<String> includes;

  private List<LoggerNode> loggers;

  private List<TriggerNode> triggers;

  private List<ListenerNode> listeners;

  public List<String> getIncludes() {
    if (includes == null) {
      includes = new ArrayList<String>();
    }
    return includes;
  }

  public List<ListenerNode> getListeners() {
    if (listeners == null) {
      listeners = new ArrayList<ListenerNode>();
    }
    return listeners;
  }

  public List<LoggerNode> getLoggers() {
    if (loggers == null) {
      loggers = new ArrayList<LoggerNode>();
    }
    return loggers;
  }

  public String getName() {
    return name;
  }

  public Properties getProperties() {
    if (properties == null) {
      properties = new Properties();
    }
    return properties;
  }

  public List<TriggerNode> getTriggers() {
    if (triggers == null) {
      triggers = new ArrayList<TriggerNode>();
    }
    return triggers;
  }

  public void setIncludes(final List<String> includes) {
    this.includes = includes;
  }

  public void setListeners(final List<ListenerNode> listeners) {
    this.listeners = listeners;
  }

  public void setLoggers(final List<LoggerNode> loggers) {
    this.loggers = loggers;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setProperties(final Properties properties) {
    this.properties = properties;
  }

  public void setTriggers(final List<TriggerNode> triggers) {
    this.triggers = triggers;
  }

  public String toString() {
    return getClass().getSimpleName() +
        "{name=" + getName() +
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
