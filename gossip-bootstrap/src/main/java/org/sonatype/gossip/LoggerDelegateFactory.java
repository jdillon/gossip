/*
 * Copyright (c) 2009-2013 the original author or authors.
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
package org.sonatype.gossip;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Creates {@link Logger} delegates.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.6
 */
public final class LoggerDelegateFactory
{
    /**
     * Returns {@link Logger} instances delegating to the given logger.  Returned instances also implement {@link LoggerDelegateAware}
     * to allow inspection/replacement of the delegate logger.
     */
    public static Logger create(final Logger target) {
        return (Logger) Proxy.newProxyInstance(target.getClass().getClassLoader(),
            new Class[]{ Logger.class, LoggerDelegateAware.class },
            new DelegateHandler(target));
    }

    public static interface LoggerDelegateAware
    {
        Logger getDelegate();

        void setDelegate(Logger delegate);
    }

    private static class DelegateHandler
        implements InvocationHandler, LoggerDelegateAware
    {
        private Logger delegate;

        private DelegateHandler(final Logger delegate) {
            setDelegate(delegate);
        }

        public Logger getDelegate() {
            return delegate;
        }

        public void setDelegate(final Logger delegate) {
            if (delegate == null) {
                throw new NullPointerException();
            }
            this.delegate = delegate;
        }

        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if (method.getDeclaringClass() == LoggerDelegateAware.class) {
                return method.invoke(this, args);
            }
            return method.invoke(getDelegate(), args);
        }
    }
}