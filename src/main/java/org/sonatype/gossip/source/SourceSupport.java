/*
 * Copyright (C) 2009 the original author or authors.
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

package org.sonatype.gossip.source;

import org.slf4j.Logger;
import org.sonatype.gossip.Log;
import org.sonatype.gossip.model.Model;
import org.sonatype.gossip.model.io.props.GossipPropertiesReader;

import java.io.File;
import java.net.URL;

/**
 * Support for {@link Source} components.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class SourceSupport
    implements Source
{
    protected final Logger log = Log.getLogger(getClass());

    protected Model load(final URL url) throws Exception {
        assert url != null;

        log.debug("Loading model from: {}", url);

        // HACK: FOr now just assume we are doing properties, support xpp3 optionally in the future
        GossipPropertiesReader reader = new GossipPropertiesReader();
        return reader.read(url);
    }

    protected Model load(final File file) throws Exception {
        assert file != null;

        if (!file.exists()) {
            log.trace("File does not exist; skipping: {}", file);
            return null;
        }

        return load(file.toURI().toURL());
    }
}