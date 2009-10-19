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

package org.sonatype.gossip.model.io.props;

import org.sonatype.gossip.Log;
import org.sonatype.gossip.model.ComponentFactory;

import java.lang.reflect.Method;

/**
 * Configures components from {@link ConfigurationContext}
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ConfigurationContextConfigurator
{
    private static Log log = Log.getLogger(ConfigurationContextConfigurator.class);

    public void configure(final Object target, final ConfigurationContext config) throws Exception {
        assert target != null;
        assert config != null;

        for (String name : config.names()) {
            // Get the first element of the name for the key
            int i = name.indexOf(".");
            if (i != -1) {
                name = name.substring(0, i);
            }

            String value = config.get(name, (String)null);

            // Attempt to set the simple value
            if (!maybeSet(target, name, value)) {
                // otherwise assume the value is class to build and inject
                Object obj = ComponentFactory.build(value, config.child(name));
                Class type = obj.getClass();
                name = "set" + capitalise(name);

                // find a method suitable to call
                for (Method method : target.getClass().getMethods()) {
                    if (method.getName().equals(name)) {
                        Class[] args = method.getParameterTypes();
                        if (args.length == 1) {
                            for (Class arg : args) {
                                if (arg.isAssignableFrom(type)) {
                                    method.invoke(target, obj);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean maybeSet(final Object target, final String name, final Object value) {
        assert target != null;
        assert name != null;
        assert value != null;

        String tmp = "set" + capitalise(name);
        Class type = target.getClass();

        try {
            Method setter = type.getMethod(tmp, value.getClass());

            if (setter != null) {
                log.trace("Setting '{}={}' via: {}", name, value, setter);
                setter.invoke(target, value);
                return true;
            }
        }
        catch (NoSuchMethodException e) {
            // ignore
        }
        catch (Exception e) {
            log.error("Failed to set '{}={}'", name, value, e);
        }

        return false;
    }

    private static String capitalise(final String str) {
        assert str != null && str.length() != 0;

        return new StringBuilder(str.length())
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}