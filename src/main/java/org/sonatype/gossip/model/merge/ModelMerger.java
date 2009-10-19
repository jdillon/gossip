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

package org.sonatype.gossip.model.merge;

import org.sonatype.gossip.model.Model;
import org.sonatype.gossip.model.ProfileNode;
import org.sonatype.gossip.model.SourceNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Handles merging {@link Model} objects.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ModelMerger
{
    public void merge(Model target, Model source, boolean sourceDominant, Map<?, ?> hints) {
        assert target != null;

        if (source == null) {
            return;
        }

        Map<Object, Object> context = new HashMap<Object, Object>();
        if (hints != null) {
            context.putAll(hints);
        }

        mergeModel(target, source, sourceDominant, context);
    }

    protected void mergeModel(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        mergeModel_Version(target, source, sourceDominant, context);
        mergeModel_Properties(target, source, sourceDominant, context);
        mergeModel_Sources(target, source, sourceDominant, context);
        mergeModel_Profiles(target, source, sourceDominant, context);
    }

    protected void mergeModel_Version(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        /*
        Version is not inherited.
        
        String src = source.getVersion();
        if (src != null) {
            if (sourceDominant || target.getVersion() == null) {
                target.setVersion(src);
            }
        }
        */
    }

    protected void mergeModel_Properties(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        Properties merged = new Properties();
        if (sourceDominant) {
            merged.putAll(target.getProperties());
            merged.putAll(source.getProperties());
        }
        else {
            merged.putAll(source.getProperties());
            merged.putAll(target.getProperties());
        }
        target.setProperties(merged);
    }

    protected void mergeModel_Sources(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        List<SourceNode> src = source.getSources();
        if (!src.isEmpty()) {
            List<SourceNode> tgt = target.getSources();
            Map<Object,SourceNode> merged = new LinkedHashMap<Object,SourceNode>((src.size() + tgt.size()) * 2);

            for (SourceNode element : tgt) {
                Object key = getSourceKey(element);
                merged.put(key, element);
            }

            for (SourceNode element : src) {
                Object key = getSourceKey(element);
                if (sourceDominant || !merged.containsKey(key)) {
                    merged.put(key, element);
                }
            }

            target.setSources(new ArrayList<SourceNode>(merged.values()));
        }
    }

    protected void mergeModel_Profiles(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        List<ProfileNode> src = source.getProfiles();
        if (!src.isEmpty()) {
            List<ProfileNode> tgt = target.getProfiles();
            Map<Object,ProfileNode> merged = new LinkedHashMap<Object,ProfileNode>((src.size() + tgt.size()) * 2);

            for (ProfileNode element : tgt) {
                Object key = getProfileKey(element);
                merged.put(key, element);
            }

            for (ProfileNode element : src) {
                Object key = getProfileKey(element);
                if (sourceDominant || !merged.containsKey(key)) {
                    merged.put(key, element);
                }
            }

            target.setProfiles(new ArrayList<ProfileNode>(merged.values()));
        }
    }

    protected Object getSourceKey( SourceNode object )
    {
        return object;
    }

    protected Object getProfileKey( ProfileNode object )
    {
        return object.getName();
    }
}