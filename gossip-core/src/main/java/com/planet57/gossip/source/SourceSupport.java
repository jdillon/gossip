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
package com.planet57.gossip.source;

import com.planet57.gossip.Log;
import com.planet57.gossip.model.Model;
import com.planet57.gossip.model.io.props.GossipPropertiesReader;
import org.slf4j.Logger;

import java.io.File;
import java.net.URL;

/**
 * Support for {@link Source} components.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public abstract class SourceSupport
    implements Source
{
  protected final Logger log = Log.getLogger(getClass());

  protected Model load(final URL url) throws Exception {
    assert url != null;

    log.debug("Loading model from: {}", url);

    return new GossipPropertiesReader().read(url);
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