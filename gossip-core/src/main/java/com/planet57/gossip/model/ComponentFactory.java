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

import com.planet57.gossip.Log;
import com.planet57.gossip.model.io.props.Context;
import com.planet57.gossip.model.io.props.ContextConfigurator;
import com.planet57.gossip.trigger.Trigger;
import org.slf4j.Logger;
import com.planet57.gossip.listener.Listener;
import com.planet57.gossip.source.Source;

/**
 * Constructs components from model nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ComponentFactory
{
  private static Logger log = Log.getLogger(ComponentFactory.class);

  public static Source create(final SourceNode node) throws Exception {
    return (Source) build(node);
  }

  public static Trigger create(final TriggerNode node) throws Exception {
    return (Trigger) build(node);
  }

  public static Listener create(final ListenerNode node) throws Exception {
    return (Listener) build(node);
  }

  //
  // Helpers
  //

  private static Class loadClass(final String className) throws ClassNotFoundException {
    assert className != null;

    Class type;

    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      log.trace("Using class-loader: {}", cl);
      if (cl == null) {
        // HACK: Sometimes the TCL is null (ick) but this probably not right either
        cl = ClassLoader.getSystemClassLoader();
      }
      type = cl.loadClass(className);
    }
    catch (ClassNotFoundException e) {
      // HACK: This is needed as a fallback on Maven 2.0.x and 2.2.x which does not have the TCL setup as expected
      log.trace("Falling back to Class.forName...");
      type = Class.forName(className);
    }

    log.trace("Loaded class: {}", type);

    return type;
  }

  private static Object build(final FactoryNode node) throws Exception {
    assert node != null;

    return build(node.getType(), node.getConfiguration());
  }

  public static Object build(final String className, final Object config) throws Exception {
    assert className != null;

    Class type = loadClass(className);
    Object obj = type.newInstance();

    if (config != null) {
      //
      // TODO: Support the Xpp3 configuration... w/o requiring it on the classpath for this class to function
      //

      if (config instanceof Context) {
        new ContextConfigurator().configure(obj, (Context) config);
      }
      else {
        log.error("Unsupported configuration type: " + config.getClass().getName());
      }
    }

    log.trace("Created: {}", obj);

    return obj;
  }
}