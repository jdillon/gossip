/**
 * Copyright (c) 2009-2011 the original author or authors.
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
package org.sonatype.gossip.model.io.props;

import org.slf4j.Logger;
import org.sonatype.gossip.Log;
import org.sonatype.gossip.model.ComponentFactory;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures components from {@link Context}
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ContextConfigurator
{
    private static final Logger log = Log.getLogger(ContextConfigurator.class);

    public void configure(final Object target, final Context config) throws Exception {
        assert target != null;
        assert config != null;

        for (String name : config.names()) {
            // Get the first element of the name for the key
            int i = name.indexOf(".");
            if (i != -1) {
                name = name.substring(0, i);
            }

            String value = config.get(name);

            // Attempt to set the simple value
            if (!maybeSet(target, name, value)) {
                // otherwise assume the value is class to build and inject
                Object obj = ComponentFactory.build(value, config.child(name));
                Class type = obj.getClass();
                name = getSetterName(name);

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

    private boolean maybeSet(final Object target, final String name, final String text) {
        assert target != null;
        assert name != null;
        assert text != null;

        try {
            Method setter = selectSetter(target.getClass(), getSetterName(name));

            if (setter != null) {
                Class type = setter.getParameterTypes()[0];

                log.trace("Setting '{}={}' via: {}", new Object[] { name, text, setter });

                Object value = null;
                if (type.isEnum()) {
                    //noinspection unchecked
                    value = selectEnum(type, text);
                }
                else if (type != String.class) {
                    PropertyEditor editor = PropertyEditorManager.findEditor(type);
                    if (editor != null) {
                        editor.setAsText(text);
                        value = editor.getValue();
                    }
                    else {
                        log.trace("Unable to convert value {} to {}", text, type);
                        return false;
                    }
                }

                if (value != null) {
                    log.trace("Converted value: {}", value);
                }
                else {
                    value = text;
                }

                setter.invoke(target, value);
                return true;
            }
            else {
                log.trace("Missing setter for: {}", text);
            }
        }
        catch (Exception e) {
            log.error("Failed to set '{}={}'", new Object[] { name, text, e });
        }

        return false;
    }

    private Enum<?> selectEnum(final Class<Enum> type, final String name) {
        assert type != null;
        assert name != null;

        for (Enum n : type.getEnumConstants()) {
            if (n.name().equalsIgnoreCase(name)) {
                return n;
            }
        }

        throw new IllegalArgumentException("No enum const " + type + "." + name);
    }

    private Method selectSetter(final Class type, final String name) {
        assert type != null;
        assert name != null;

        Map<Class,Method> setters = new HashMap<Class,Method>();
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterTypes().length == 1) {
                setters.put(method.getParameterTypes()[0], method);
            }
        }

        Method setter = null;
        if (setters.size() == 1) {
            setter = setters.entrySet().iterator().next().getValue();
        }
        else if (setters.size() > 1) {
            // Prefer the string setter if if there is one
            setter = setters.get(String.class);
        }

        return setter;
    }

    private String getSetterName(final String name) {
        assert name != null && name.length() != 0;

        return new StringBuilder(name.length() + 3)
            .append("set")
            .append(Character.toTitleCase(name.charAt(0)))
            .append(name.substring(1))
            .toString();
    }
}