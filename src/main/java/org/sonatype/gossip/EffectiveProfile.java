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

package org.sonatype.gossip;

import org.sonatype.gossip.Event;
import org.sonatype.gossip.model2.LoggerNode;
import org.sonatype.gossip.model2.ProfileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Effective profile.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class EffectiveProfile
{
    private final Log log = Log.getLogger(getClass());

    private List<ProfileNode> profiles = new ArrayList<ProfileNode>();

    private Map<String,LoggerNode> loggers;

    public EffectiveProfile() {}

    public List<ProfileNode> profiles() {
        return profiles;
    }

    public void addProfile(final ProfileNode node) {
        assert node != null;

        profiles().add(node);
    }

    public Map<String,LoggerNode> loggers() {
        if (loggers == null) {
            log.trace("Loading effective logger table");

            Map<String,LoggerNode> map = new HashMap<String,LoggerNode>();

            for (ProfileNode profile : profiles()) {
                for (LoggerNode node : profile.getLoggers()) {
                    map.put(node.getName(), node);
                }
            }
            this.loggers = map;
        }

        return loggers;
    }

    public void filter(final Event event) {
        assert event != null;

        log.trace("Filtering event: {}", event);

        // TODO:

        /*
        // Else execute all filters until we get a stop
        if (chain == null) {
            chain = filters().toArray(new Filter[filters.size()]);
        }

        // log.debug("Applying {} filters to event: {}", String.valueOf(chain.length), event);

        for (int i=0; i<chain.length; i++) {
            // log.debug("Applying filter[{}]: {}", String.valueOf(i), chain[i]);

            Filter.Result r = chain[i].filter(event);

            // log.debug("Filter[{}] result: ", String.valueOf(i), r);

            if (r == Filter.Result.STOP) {
                break;
            }
        }
        */
    }
}