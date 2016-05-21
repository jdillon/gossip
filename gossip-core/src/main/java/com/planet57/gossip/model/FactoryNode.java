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
 * Class FactoryNode.
 *
 * @since 1.0
 */
@SuppressWarnings("all")
public abstract class FactoryNode
    extends Node
    implements java.io.Serializable
{

  //--------------------------/
  //- Class/Member Variables -/
  //--------------------------/

  /**
   * Field type.
   */
  private String type;

  /**
   * Field configuration.
   */
  private Object configuration;


  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Get the configuration field.
   *
   * @return Object
   */
  public Object getConfiguration()
  {
    return this.configuration;
  } //-- Object getConfiguration()

  /**
   * Get the type field.
   *
   * @return String
   */
  public String getType()
  {
    return this.type;
  } //-- String getType()

  /**
   * Set the configuration field.
   */
  public void setConfiguration(Object configuration)
  {
    this.configuration = configuration;
  } //-- void setConfiguration( Object )

  /**
   * Set the type field.
   */
  public void setType(String type)
  {
    this.type = type;
  } //-- void setType( String )


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
