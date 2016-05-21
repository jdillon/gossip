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
 * Class LoggerNode.
 *
 * @since 1.0
 */
@SuppressWarnings("all")
public class LoggerNode
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
   * Field level.
   */
  private String level;


  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Get the level field.
   *
   * @return String
   */
  public String getLevel()
  {
    return this.level;
  } //-- String getLevel()

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
   * Set the level field.
   */
  public void setLevel(String level)
  {
    this.level = level;
  } //-- void setLevel( String )

  /**
   * Set the name field.
   */
  public void setName(String name)
  {
    this.name = name;
  } //-- void setName( String )


  public com.planet57.gossip.Level asLevel() {
    return com.planet57.gossip.Level.valueOf(getLevel().toUpperCase());
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
