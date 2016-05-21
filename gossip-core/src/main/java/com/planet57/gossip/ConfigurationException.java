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
package com.planet57.gossip;

/**
 * Thrown to indicate a failure has occurred while configuring Gossip.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ConfigurationException
    extends RuntimeException
{
  private static final long serialVersionUID = 1;

  public ConfigurationException() {}

  public ConfigurationException(final String message) {
    super(message);
  }

  public ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ConfigurationException(final Throwable cause) {
    super(cause);
  }
}