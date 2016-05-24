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
 * Model.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class Model
    implements Serializable
{
  private String version = "1.0.0";

  private Properties properties;

  private List<SourceNode> sources;

  private List<ProfileNode> profiles;

  private String modelEncoding = "UTF-8";

  public String getModelEncoding() {
    return modelEncoding;
  }

  public List<ProfileNode> getProfiles() {
    if (profiles == null) {
      profiles = new ArrayList<ProfileNode>();
    }
    return profiles;
  }

  public Properties getProperties() {
    if (properties == null) {
      properties = new Properties();
    }

    return properties;
  }

  public List<SourceNode> getSources() {
    if (sources == null) {
      sources = new ArrayList<SourceNode>();
    }
    return sources;
  }

  public String getVersion() {
    return version;
  }

  public void setModelEncoding(final String modelEncoding) {
    this.modelEncoding = modelEncoding;
  }

  public void setProfiles(final List<ProfileNode> profiles) {
    this.profiles = profiles;
  }

  public void setProperties(final Properties properties) {
    this.properties = properties;
  }

  public void setSources(final List<SourceNode> sources) {
    this.sources = sources;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

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
