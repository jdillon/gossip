/*
 * Copyright (C) 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.gossip.model;

import org.sonatype.gossip.Log;
import org.sonatype.gossip.MissingPropertyException;
import org.sonatype.gossip.filter.Filter;
import org.sonatype.gossip.model.io.props.ConfigurationContext;
import org.sonatype.gossip.source.Source;
import org.sonatype.gossip.trigger.Trigger;

import java.lang.reflect.Method;

/**
 * Constructs components from model nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ComponentFactory
{
    private static Log log = Log.getLogger(ComponentFactory.class);

    public static Source create(final SourceNode node) throws Exception {
        assert node != null;

        String className = node.getType();
        if (className == null) {
            throw new MissingPropertyException("type");
        }

        Source obj = (Source)create(className, node.getConfiguration());

        log.trace("Created source: {}", obj);

        return obj;
    }

    public static Trigger create(final TriggerNode node) throws Exception {
        assert node != null;

        String className = node.getType();
        if (className == null) {
            throw new MissingPropertyException("type");
        }

        Trigger obj = (Trigger)create(className, node.getConfiguration());

        log.trace("Created trigger: {}", obj);

        return  obj;
    }

    public static Filter create(final FilterNode node) throws Exception {
        assert node != null;

        String className = node.getType();
        if (className == null) {
            throw new MissingPropertyException("type");
        }

        Filter obj = (Filter)create(className, node.getConfiguration());

        log.trace("Created filter: {}", obj);

        return obj;
    }

    //
    // Helpers
    //

    private static Class loadClass(final String className) throws ClassNotFoundException {
        assert className != null;

        Class type = Thread.currentThread().getContextClassLoader().loadClass(className);

        log.trace("Loaded class: {}", type);

        return type;
    }

    private static Object create(final String className, final Object config) throws Exception {
        assert className != null;

        Class type = loadClass(className);
        Object obj = type.newInstance();

        if (config != null) {
            //
            // TODO: Support the Xpp3 configuration... w/o requiring it on the classpath for this class to function
            //

            if (config instanceof ConfigurationContext) {
                configure(obj, (ConfigurationContext)config);
            }
            else {
                log.error("Unsupported configuration type: " + config.getClass().getName());
            }
        }

        return obj;
    }

    private static void configure(final Object obj, final ConfigurationContext config) {
        assert obj != null;
        assert config != null;

        //
        // TODO: Need to support some form of nested configuration mainly to configure renderers
        ///

        for (String name : config.names()) {
            // Get the first element of the name for the key
            int i = name.indexOf(".");
            if (i != -1) {
                name = name.substring(0, i);
            }

            String value = config.get(name, (String)null);

            maybeSet(obj, name, value);
        }
    }

    private static void maybeSet(final Object target, final String name, final Object value) {
        assert target != null;
        assert name != null;
        assert value != null;

        String tmp = "set" + capitalise(name);

        log.trace("Looking for setter: {}", tmp);

        Class type = target.getClass();

        try {
            Method setter = type.getMethod(tmp, new Class[] { String.class });

            if (setter != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Setting '{}={}' via: {}", new Object[] { name, value, setter });
                }

                setter.invoke(target, value);
            }
        }
        catch (NoSuchMethodException e) {
            log.warn("No '{}(String)' found for: {} in: {}", new Object[] { tmp, name, type });
        }
        catch (Exception e) {
            log.error("Failed to set '{}={}'", new Object[] { name, value, e });
        }
    }

    private static String capitalise(final String str) {
        if (str == null) {
            return null;
        }
        else if (str.length() == 0) {
            return str;
        }
        else {
            return new StringBuilder(str.length())
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
        }
    }
}