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
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.spi.LoggerFactoryBinder;
import com.planet57.gossip.Log;

/**
 * Gossip Bootstrap logger binder for SLF4J.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class StaticLoggerBinder
    implements LoggerFactoryBinder
{
  public static String REQUESTED_API_VERSION = "1.6.0";  // to avoid constant folding by the compiler, this field must *not* be final

  private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

  /**
   * @return {@link #SINGLETON}
   */
  public static StaticLoggerBinder getSingleton() {
    return SINGLETON;
  }

  private final ILoggerFactory factory = new ILoggerFactory()
  {
    public Logger getLogger(final String name) {
      return Log.getLogger(name);
    }
  };

  public ILoggerFactory getLoggerFactory() {
    Log.configure(factory);
    return factory;
  }

  public String getLoggerFactoryClassStr() {
    return factory.getClass().getName();
  }
}