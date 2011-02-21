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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
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
public final class Context
    implements Cloneable
{
    private static final String NEWLINE = System.getProperty("line.separator");

    private final Map<String,String> store;

    private String prefix;

    private Context parent;

    private Context(final Map<String,String> store, final String prefix) {
        assert store != null;
        this.store = store;
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();

        buff.append(getClass().getSimpleName());
        buff.append("[").append(NEWLINE);

        if (prefix != null) {
            for (Map.Entry<String,String> entry : store.entrySet()) {
                if (entry.getKey().startsWith(prefix)) {
                    buff.append("  ").
                        append(entry.getKey()).append("=").append(entry.getValue()).
                        append(",").append(NEWLINE);
                }
            }
        }

        buff.append("]");

        return buff.toString();
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

    public String set(final String name, final String value) {
        assert name != null;
        assert value != null;

        return store.put(key(name), value);
    }

    public String get(final String name, final String defaultValue) {
        assert name != null;
        // defaultValue can be null

        String value = store.get(key(name));

        if (value == null) {
            value =  defaultValue;
        }

        return value;
    }

    public String get(final String name) {
        return get(name, null);
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

    public Context parent() {
        if (parent == null) {
            throw new IllegalStateException("Parent is not bound");
        }

        return parent;
    }

    public Context child(final String prefix) {
        assert prefix != null;

        Context child = (Context) clone();

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
    // Helpers
    //

    @SuppressWarnings({"unchecked"})
    public static Context create(final URL input) throws IOException {
        assert input != null;

        InputStream in = input.openStream();
        Properties props;
        try {
            props = new Properties();
            if (input.getFile().toLowerCase().endsWith(".xml")) {
                props.loadFromXML(in);
            }
            props.load(in);
        }
        finally {
            in.close();
        }

        Context ctx = new Context((Map)props, null);

        return ctx;
    }
    public static Properties asProperties(final Context ctx) {
        assert ctx != null;

        Properties props = new Properties();

        for (Iterator iter = ctx.names().iterator(); iter.hasNext();) {
            String key = (String)iter.next();
            String value = ctx.get(key);
            props.setProperty(key, value);
        }

        return props;
    }

    public Properties toProperties() {
        return asProperties(this);
    }

    public String[] split(final String name, boolean trim) {
        String[] strings = this.get(name, "").split(",");

        if (trim) {
            strings = trim(strings);
        }

        return strings;
    }

    public static String[] trim(final String[] strings) {
        for (int i=0; i<strings.length; i++) {
            strings[i] = strings[i].trim();
        }

        return strings;
    }
}