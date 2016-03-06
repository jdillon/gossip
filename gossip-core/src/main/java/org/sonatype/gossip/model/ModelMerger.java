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
package org.sonatype.gossip.model;

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
        mergeModel_Properties(target, source, sourceDominant, context);
        mergeModel_Profiles(target, source, sourceDominant, context);
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

    protected void mergeModel_Profiles(Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
        List<ProfileNode> src = source.getProfiles();
        if (!src.isEmpty()) {
            List<ProfileNode> profiles = target.getProfiles();
            Map<Object,ProfileNode> merged = new LinkedHashMap<Object,ProfileNode>((src.size() + profiles.size()) * 2);

            for (ProfileNode element : profiles) {
                Object key = element.getName();
                merged.put(key, element);
            }

            for (ProfileNode element : src) {
                Object key = element.getName();
                if (sourceDominant || !merged.containsKey(key)) {
                    merged.put(key, element);
                }
            }

            target.setProfiles(new ArrayList<ProfileNode>(merged.values()));
        }
    }
}