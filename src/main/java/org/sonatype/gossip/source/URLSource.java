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

package org.sonatype.gossip.source;

import org.sonatype.gossip.MissingPropertyException;
import org.sonatype.gossip.model.Model;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL configuration source.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class URLSource
    extends SourceSupport
{
    private URL url;

    public void setUrl(final URL url) {
        this.url = url;
    }
    
    public void setUrl(final String location) throws MalformedURLException {
        assert location != null;

        setUrl(new URL(location));
    }

    public URL getUrl() {
        return url;
    }

    public Model load() throws Exception {
        if (url == null) {
            throw new MissingPropertyException("url");
        }

        return load(url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "url=" + url +
                '}';
    }
}