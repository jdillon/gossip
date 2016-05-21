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
package com.planet57.gossip.support;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Diagnostic context; provides map and stack facilities.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public class DC
{
    /**
     * The key in which we <em>publish</em> our contents to in the Slf4j MDC.
     */
    public static final String KEY = getProperty("key", "DC");

    /**
     * String prefixed to MDC value when there is content to publish.
     *
     * @since 1.7
     */
    public static final String PREFIX = getProperty("prefix", ",");

    /**
     * String used to separate the stack and context values when both are present.
     *
     * @since 1.7
     */
    public static final String SEPARATOR = getProperty("separator", ",");

    /**
     * Container for thread diagnostic context state.
     *
     * @since 1.7
     */
    public static class State
    {
        private final Map<String, String> map;

        private final LinkedList<String> stack;

        private State() {
            this.map = new HashMap<String,String>();
            this.stack = new LinkedList<String>();
        }

        private State(final State source) {
            this();
            checkNotNull(source);
            this.map.putAll(source.map);
            this.stack.addAll(source.stack);
        }

        @Override
        public String toString() {
            return "State{" +
                "map=" + map +
                ", stack=" + stack +
                '}';
        }
    }

    private static InheritableThreadLocal<State> stateHolder = new InheritableThreadLocal<State>()
    {
        @Override
        protected State initialValue() {
            return new State();
        }

        @Override
        protected State childValue(final State parentValue) {
            return new State(parentValue);
        }
    };

    private static State state() {
        return stateHolder.get();
    }

    /**
     * Helper to get {@link State#map}.
     */
    private static Map<String,String> map() {
        return state().map;
    }

    /**
     * Helper to get {@link State#stack}.
     */
    private static LinkedList<String> stack() {
        return state().stack;
    }

    /**
     * Get a copy of the state for the current thread.
     *
     * @since 1.7
     */
    public static State getState() {
        return new State(state());
    }

    /**
     * Set the state for the current thread.
     *
     * @since 1.7
     */
    public static void setState(final State state) {
        checkNotNull(state);
        stateHolder.set(state);
        update();
    }

    /**
     * Reset the current threads state.
     *
     * @since 1.7
     */
    public static void reset() {
        stateHolder.remove();
        MDC.remove(KEY);
    }

    /**
     * Render the current threads state.
     *
     * @since 1.7
     */
    public static StringBuilder render() {
        State state = state();
        StringBuilder buff = new StringBuilder();

        // Append the stack if there is one
        if (!state.stack.isEmpty()) {
            buff.append(state.stack);
        }

        if (!state.map.isEmpty()) {
            // Append the context if there is some
            if (buff.length() != 0) {
                buff.append(SEPARATOR);
            }
            buff.append(state.map);
        }

        return buff;
    }

    /**
     * Updates and <em>publishes</em> the rendered state to the Slf4j MDC.
     */
    private static void update() {
        StringBuilder buff = render();
        if (buff.length() != 0) {
            buff.insert(0, PREFIX);
            MDC.put(KEY, buff.toString());
        }
        else {
            reset();
        }
    }

    // Context operations

    public static void put(final Object key, final Object value) {
        checkNotNull(key);
        map().put(key.toString(), String.valueOf(value));
        update();
    }

    public static void put(final Class key, final Object value) {
        checkNotNull(key);
        put(key.getSimpleName(), value);
    }

    /**
     * @since 1.8
     */
    public static interface RestoreHandle
    {
        /**
         * Restore previously set value or remove if previously unset.
         */
        void restore();
    }

    /**
     * @since 1.8
     */
    public static RestoreHandle install(final Object key, final Object value) {
        checkNotNull(key);
        final String _key = String.valueOf(key);
        final Map<String,String> map = map();
        final boolean exists = map.containsKey(_key);
        final Object prev = map().put(_key, String.valueOf(value));
        update();
        return new RestoreHandle()
        {
            public void restore() {
                if (exists) {
                    map.put(_key, String.valueOf(prev));
                }
                else {
                    map.remove(_key);
                }
                update();
            }
        };
    }

    /**
     * @since 1.8
     */
    public static RestoreHandle install(final Class key, final Object value) {
        checkNotNull(key);
        return install(key.getSimpleName(), value);
    }

    public static String get(final Object key) {
        checkNotNull(key);
        return map().get(key.toString());
    }

    public static String get(final Class key) {
        checkNotNull(key);
        return get(key.getSimpleName());
    }

    public static void remove(final Object key) {
        checkNotNull(key);
        map().remove(key.toString());
        update();
    }

    public static void remove(final Class key) {
        checkNotNull(key);
        remove(key.getSimpleName());
    }

    // Stack Operations
    
    public static void push(final Object value) {
        checkNotNull(value);
        stack().addFirst(value.toString());
        update();
    }
    
    public static String pop() {
        String value = stack().removeFirst();
        update();
        return value;
    }
    
    public static String peek() {
        return stack().peek();
    }

    // Misc Helpers

    private static String getProperty(final String name, final String defaultValue) {
        return System.getProperty(DC.class.getName() + "." + name, defaultValue);
    }

    private static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}