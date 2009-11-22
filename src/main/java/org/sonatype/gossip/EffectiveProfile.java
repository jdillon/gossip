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

package org.sonatype.gossip;

import org.slf4j.Logger;
import org.sonatype.gossip.listener.Listener;
import org.sonatype.gossip.model.ListenerNode;
import org.sonatype.gossip.model.LoggerNode;
import org.sonatype.gossip.model.ProfileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Effective profile.  This handles what is currently configured/activated.  The meat of event dispatching is done here.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public final class EffectiveProfile
{
    private static final Logger log = Log.getLogger(EffectiveProfile.class);

    private final List<ProfileNode> profiles = new ArrayList<ProfileNode>();

    private Map<String,LoggerNode> loggers;

    public List<ProfileNode> getProfiles() {
        return profiles;
    }

    public void addProfile(final ProfileNode node) {
        assert node != null;
        getProfiles().add(node);
    }

    public Map<String,LoggerNode> loggers() {
        if (loggers == null) {
            log.trace("Loading effective logger table");

            Map<String,LoggerNode> map = new HashMap<String,LoggerNode>();

            for (ProfileNode profile : getProfiles()) {
                for (LoggerNode node : profile.getLoggers()) {
                    map.put(node.getName(), node);
                }
            }
            this.loggers = map;
        }

        return loggers;
    }

    private Listener[] listeners;

    public void dispatch(Event event) {
        assert event != null;

        if (this.listeners == null) {
            log.trace("Building listener dispatch table");

            List<Listener> listeners = new ArrayList<Listener>();
            for (ProfileNode profile : getProfiles()) {
                for (ListenerNode listener : profile.getListeners()) {
                    try {
                        log.trace("Adding listener: {}", listener);
                        listeners.add(listener.create());
                    }
                    catch (Exception e) {
                        log.error("Failed to create listener: " + listener, e);
                    }
                }
            }

            this.listeners = listeners.toArray(new Listener[listeners.size()]);
        }

        log.trace("Dispatching event to {} listener(s): {}", listeners.length, event);

        int i=0;
        for (Listener listener : listeners) {
            log.trace("Dispatching to listener[{}]: {}", i, listener);
            try {
                listener.onEvent(event);
            }
            catch (Throwable t) {
                log.error("Listener execution failed; ignoring", t);
            }
            i++;
        }
    }
}