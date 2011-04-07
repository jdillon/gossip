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

package org.sonatype.gossip.support;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

/**
 * Diagnostic context; provides map and stack facilities.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public class DC
{
    /** The key in which we <em>publish</em> our contents to in the Slf4j MDC. */
    public static final String KEY = System.getProperty(DC.class.getName() + ".key", "DC");

    // NOTE: Collections returned still must be thread-safe since they are inherited by child-threads

    private static InheritableThreadLocal<Map<String,String>> contextHolder = new InheritableThreadLocal<Map<String,String>>()
    {
        @Override
        protected Map<String, String> initialValue() {
            return new Hashtable<String,String>();
        }
    };

    private static Map<String,String> context() {
        return contextHolder.get();
    }
    
    private static InheritableThreadLocal<Stack<String>> stackHolder = new InheritableThreadLocal<Stack<String>>()
    {
        @Override
        protected Stack<String> initialValue() {
            return new Stack<String>();
        }
    };
    
    private static Stack<String> stack() {
        return stackHolder.get();
    }

    /**
     * Updates and <em>publishes</em> our context+stack to the Slf4j MDC.
     */
    private static void update() {
        StringBuilder buff = new StringBuilder();

        // Append the stack if there is one
        Stack stack = stack();
        if (!stack.isEmpty()) {
            buff.append(stack);
        }

        Map context = context();
        if (!context.isEmpty()) {
            // Append the context if there is some
            if (buff.length() != 0) {
                buff.append(",");
            }
            buff.append(context);
        }

        // If we have something to publish, then do it
        if (buff.length() != 0) {
            buff.insert(0, ",");
            MDC.put(KEY, buff.toString());
        }
        else {
            // Else remove what was their before
            MDC.remove(KEY);
        }
    }

    // Context operations

    public static void put(final Object key, final Object value) {
        assert key != null;
        context().put(key.toString(), String.valueOf(value));
        update();
    }

    public static void put(final Class key, final Object value) {
        assert key != null;
        put(key.getSimpleName(), value);
    }

    public static String get(final Object key) {
        assert key != null;
        return context().get(key.toString());
    }

    public static String get(final Class key) {
        assert key != null;
        return get(key.getSimpleName());
    }

    public static void remove(final Object key) {
        assert key != null;
        context().remove(key.toString());
        update();
    }

    public static void remove(final Class key) {
        assert key != null;
        remove(key.getSimpleName());
    }

    // Stack Operations
    
    public static void push(final Object value) {
        assert value != null;
        stack().push(value.toString());
        update();
    }
    
    public static String pop() {
        String value = stack().pop();
        update();
        return value;
    }
    
    public static String peek() {
        return stack().peek();
    }
}