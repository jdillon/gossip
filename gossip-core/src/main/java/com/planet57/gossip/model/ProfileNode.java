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

/**
 * Class ProfileNode.
 *
 * @since 1.0
 */
@SuppressWarnings("all")
public class ProfileNode
    extends Node
    implements java.io.Serializable
{

  //--------------------------/
  //- Class/Member Variables -/
  //--------------------------/

  /**
   * Field name.
   */
  private String name;

  /**
   * Field properties.
   */
  private java.util.Properties properties;

  /**
   * Field includes.
   */
  private java.util.List<String> includes;

  /**
   * Field loggers.
   */
  private java.util.List<LoggerNode> loggers;

  /**
   * Field triggers.
   */
  private java.util.List<TriggerNode> triggers;

  /**
   * Field listeners.
   */
  private java.util.List<ListenerNode> listeners;


  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Method getIncludes.
   *
   * @return List
   */
  public java.util.List<String> getIncludes()
  {
    if (this.includes == null) {
      this.includes = new java.util.ArrayList<String>();
    }

    return this.includes;
  } //-- java.util.List<String> getIncludes()

  /**
   * Method getListeners.
   *
   * @return List
   */
  public java.util.List<ListenerNode> getListeners()
  {
    if (this.listeners == null) {
      this.listeners = new java.util.ArrayList<ListenerNode>();
    }

    return this.listeners;
  } //-- java.util.List<ListenerNode> getListeners()

  /**
   * Method getLoggers.
   *
   * @return List
   */
  public java.util.List<LoggerNode> getLoggers()
  {
    if (this.loggers == null) {
      this.loggers = new java.util.ArrayList<LoggerNode>();
    }

    return this.loggers;
  } //-- java.util.List<LoggerNode> getLoggers()

  /**
   * Get the name field.
   *
   * @return String
   */
  public String getName()
  {
    return this.name;
  } //-- String getName()

  /**
   * Method getProperties.
   *
   * @return Properties
   */
  public java.util.Properties getProperties()
  {
    if (this.properties == null) {
      this.properties = new java.util.Properties();
    }

    return this.properties;
  } //-- java.util.Properties getProperties()

  /**
   * Method getTriggers.
   *
   * @return List
   */
  public java.util.List<TriggerNode> getTriggers()
  {
    if (this.triggers == null) {
      this.triggers = new java.util.ArrayList<TriggerNode>();
    }

    return this.triggers;
  } //-- java.util.List<TriggerNode> getTriggers()

  /**
   * Set the includes field.
   */
  public void setIncludes(java.util.List<String> includes)
  {
    this.includes = includes;
  } //-- void setIncludes( java.util.List )

  /**
   * Set the listeners field.
   */
  public void setListeners(java.util.List<ListenerNode> listeners)
  {
    this.listeners = listeners;
  } //-- void setListeners( java.util.List )

  /**
   * Set the loggers field.
   */
  public void setLoggers(java.util.List<LoggerNode> loggers)
  {
    this.loggers = loggers;
  } //-- void setLoggers( java.util.List )

  /**
   * Set the name field.
   */
  public void setName(String name)
  {
    this.name = name;
  } //-- void setName( String )

  /**
   * Set the properties field.
   */
  public void setProperties(java.util.Properties properties)
  {
    this.properties = properties;
  } //-- void setProperties( java.util.Properties )

  /**
   * Set the triggers field.
   */
  public void setTriggers(java.util.List<TriggerNode> triggers)
  {
    this.triggers = triggers;
  } //-- void setTriggers( java.util.List )


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
