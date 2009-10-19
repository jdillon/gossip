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

import org.sonatype.gossip.ConfigurationException;
import org.sonatype.gossip.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Container for Gossip configuration details.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public final class ConfigurationContext
    implements Cloneable
{
    private final Log log = Log.getLogger(getClass());

    private Map<String,Object> store;

    private String prefix;

    private ConfigurationContext parent;

    public ConfigurationContext(final Map<String,Object> store, final String prefix) {
        assert store != null;
        this.store = store;
        this.prefix = prefix;
    }

    public ConfigurationContext(final Map<String,Object> store) {
        this(store, null);
    }

    @SuppressWarnings({"unchecked"})
    public ConfigurationContext(final Properties store) {
        this((Map)store, null);
    }

    public ConfigurationContext() {
        this(new HashMap<String,Object>(), null);
    }

    public ConfigurationContext(final ConfigurationContext config) {
        this(config.store, config.prefix);
    }

    @Override
    public String toString() {
        if (prefix == null) {
            return getClass().getSimpleName() + "[]: " + store;
        }

        return getClass().getSimpleName() + "[" + prefix + "]: " + store;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    void dump() {
        dump("    ");
    }

    void dump(final String pad) {
        assert pad != null;

        for (Map.Entry<String,Object> entry : store.entrySet()) {
            log.debug("{}{}={}", pad, entry.getKey(), entry.getValue());
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String key(final String name) {
        assert name != null;

        if (prefix != null) {
            return prefix + "." + name;
        }

        return name;
    }

    public boolean contains(final String name) {
        assert name != null;

        return store.containsKey(key(name));
    }

    public Object set(final String name, final Object value) {
        assert name != null;
        assert value != null;

        return store.put(key(name), value);
    }

    public Object get(final String name, final Object defaultValue) {
        assert name != null;
        // defaultValue can be null

        Object value = store.get(key(name));

        if (value == null) {
            value =  defaultValue;
        }

        return value;
    }

    public Object get(final String name) {
        return get(name, (Object)null);
    }

    public int size() {
        if (prefix == null) {
            return store.size();
        }

        int c = 0;

        for (String key : store.keySet()) {
            if (key.startsWith(prefix)) {
                c++;
            }
        }

        return c;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Set<String> names() {
        if (prefix == null) {
            return Collections.unmodifiableSet(store.keySet());
        }

        Set<String> matching = new HashSet<String>();
        int l = prefix.length();

        for (String key : store.keySet()) {
            if (key.startsWith(prefix + ".")) {
                // Strip off the prefix
                key = key.substring(l + 1, key.length());
                matching.add(key);
            }
        }

        return Collections.unmodifiableSet(matching);
    }

    //
    // Children
    //

    public ConfigurationContext parent() {
        if (parent == null) {
            throw new IllegalStateException("Parent is not bound");
        }

        return parent;
    }

    public ConfigurationContext child(final String prefix) {
        assert prefix != null;

        ConfigurationContext child = (ConfigurationContext) clone();

        child.parent = this;

        if (child.prefix != null) {
            child.prefix += "." + prefix;
        }
        else {
            child.prefix = prefix;
        }

        return child;
    }

    //
    // Typed Access
    //

    public Object set(final String name, final boolean value) {
        return set(name, Boolean.valueOf(value));
    }

    public boolean get(final String name, final boolean defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Boolean) {
            return (Boolean)value;
        }

        return Boolean.valueOf(String.valueOf(value));
    }

    public Object set(final String name, final int value) {
        return set(name, new Integer(value));
    }

    public int get(final String name, final int defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Number) {
            return ((Number)value).intValue();
        }

        return Integer.valueOf(String.valueOf(value));
    }

    public String get(final String name, final String defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof String) {
            return (String)value;
        }

        return String.valueOf(value);
    }

    public File get(final String name, final File defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof File) {
            return (File)value;
        }

        return new File(String.valueOf(value));
    }

    public URL get(final String name, final URL defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof URL) {
            return (URL)value;
        }

        try {
            return new URL(String.valueOf(value));
        }
        catch (MalformedURLException e) {
            throw new ConfigurationException("Unable to decode URL; name=" + name + ", value=" + value, e);
        }
    }

    public URI get(final String name, final URI defaultValue) {
        Object value = get(name);

        if (value == null) {
            return defaultValue;
        }

        if (value instanceof URI) {
            return (URI)value;
        }

        try {
            return new URI(String.valueOf(value));
        }
        catch (URISyntaxException e) {
            throw new ConfigurationException("Unable to decode URI; name=" + name + ", value=" + value, e);
        }
    }

    //
    // Helpers
    //

    public static ConfigurationContext create(final InputStream input) throws IOException {
        assert input != null;

        Properties props = new Properties();
        props.load(input);
        ConfigurationContext ctx = new ConfigurationContext(props);

        return ctx;
    }

    public static Properties asProperties(final ConfigurationContext ctx) {
        assert ctx != null;

        Properties props = new Properties();

        for (Iterator iter = ctx.names().iterator(); iter.hasNext();) {
            String key = (String)iter.next();
            String value = ctx.get(key, (String)null);

            props.setProperty(key, value);
        }

        return props;
    }

    public Properties toProperties() {
        return asProperties(this);
    }

    public String[] split(final String name) {
        return this.get(name, "").split(",");
    }
}