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
 * Class Node.
 *
 * @since 1.0
 */
@SuppressWarnings("all")
public abstract class Node
    implements java.io.Serializable
{

  //--------------------------/
  //- Class/Member Variables -/
  //--------------------------/

  /**
   * Field id.
   */
  private String id;


  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Get the id field.
   *
   * @return String
   */
  public String getId()
  {
    return this.id;
  } //-- String getId()

  /**
   * Set the id field.
   */
  public void setId(String id)
  {
    this.id = id;
  } //-- void setId( String )


  public int hashCode() {
    return getId().hashCode();
  }


  public boolean equals(final Object target) {
    if (target instanceof Node) {
      return ((Node) target).getId().equals(this.getId());
    }
    return false;
  }

}
