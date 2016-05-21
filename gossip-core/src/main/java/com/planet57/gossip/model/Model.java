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
 * Class Model.
 *
 * @since 1.0
 */
@SuppressWarnings("all")
public class Model
    implements java.io.Serializable
{

  //--------------------------/
  //- Class/Member Variables -/
  //--------------------------/

  /**
   * Field version.
   */
  private String version = "1.0.0";

  /**
   * Field properties.
   */
  private java.util.Properties properties;

  /**
   * Field sources.
   */
  private java.util.List<SourceNode> sources;

  /**
   * Field profiles.
   */
  private java.util.List<ProfileNode> profiles;

  /**
   * Field modelEncoding.
   */
  private String modelEncoding = "UTF-8";


  //-----------/
  //- Methods -/
  //-----------/

  /**
   * Get the modelEncoding field.
   *
   * @return String
   */
  public String getModelEncoding()
  {
    return this.modelEncoding;
  } //-- String getModelEncoding()

  /**
   * Method getProfiles.
   *
   * @return List
   */
  public java.util.List<ProfileNode> getProfiles()
  {
    if (this.profiles == null) {
      this.profiles = new java.util.ArrayList<ProfileNode>();
    }

    return this.profiles;
  } //-- java.util.List<ProfileNode> getProfiles()

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
   * Method getSources.
   *
   * @return List
   */
  public java.util.List<SourceNode> getSources()
  {
    if (this.sources == null) {
      this.sources = new java.util.ArrayList<SourceNode>();
    }

    return this.sources;
  } //-- java.util.List<SourceNode> getSources()

  /**
   * Get the version field.
   *
   * @return String
   */
  public String getVersion()
  {
    return this.version;
  } //-- String getVersion()

  /**
   * Set the modelEncoding field.
   */
  public void setModelEncoding(String modelEncoding)
  {
    this.modelEncoding = modelEncoding;
  } //-- void setModelEncoding( String )

  /**
   * Set the profiles field.
   */
  public void setProfiles(java.util.List<ProfileNode> profiles)
  {
    this.profiles = profiles;
  } //-- void setProfiles( java.util.List )

  /**
   * Set the properties field.
   */
  public void setProperties(java.util.Properties properties)
  {
    this.properties = properties;
  } //-- void setProperties( java.util.Properties )

  /**
   * Set the sources field.
   */
  public void setSources(java.util.List<SourceNode> sources)
  {
    this.sources = sources;
  } //-- void setSources( java.util.List )

  /**
   * Set the version field.
   */
  public void setVersion(String version)
  {
    this.version = version;
  } //-- void setVersion( String )


  public ProfileNode findProfile(final String name) {
    assert name != null;
    for (ProfileNode profile : getProfiles()) {
      if (profile.getName().trim().equals(name)) {
        return profile;
      }
    }
    return null;
  }

}
