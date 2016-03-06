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
package org.sonatype.gossip.support;

import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Factory to produce a multiplexing {@link Logger}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public class MuxLoggerFactory
{
    /**
     * @param loggers The first logger will be responsible for all methods which return values, otherwise all loggers are invoked in order.
     */
    public static Logger create(final Logger... loggers) {
        return (Logger) Proxy.newProxyInstance(Logger.class.getClassLoader(), new Class[]{Logger.class}, new Handler(loggers));
    }

    private static class Handler
        implements InvocationHandler
    {
        private final Logger[] loggers;

        private Handler(final Logger[] loggers) {
            assert loggers != null;
            assert loggers.length > 0;
            this.loggers = loggers;
        }

        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            assert proxy != null;
            assert method != null;

            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            else {
                // For methods that return something, pick the first logger (mostly name + isEnabled() muck)
                if (method.getReturnType() != Void.TYPE) {
                    return method.invoke(loggers[0], args);
                }

                // Else invoke them all
                for (Logger logger : loggers) {
                    method.invoke(logger, args);
                }

                return null; // void
            }
        }
    }
}