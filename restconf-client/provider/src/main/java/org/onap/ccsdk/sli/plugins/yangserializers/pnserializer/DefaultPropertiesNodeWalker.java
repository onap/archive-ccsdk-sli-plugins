/*-
 * ============LICENSE_START=======================================================
 * ONAP - CCSDK
 * ================================================================================
 * Copyright (C) 2018 Huawei Technologies Co., Ltd. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.sli.plugins.yangserializers.pnserializer;

import org.onap.ccsdk.sli.core.sli.SvcLogicException;

import java.util.Map;

/**
 * Implementation of properties node walker which helps in forming a new tree from properties node.
 *
 * @param <T> node child of properties node.
 */
public class DefaultPropertiesNodeWalker<T extends NodeChild> implements PropertiesNodeWalker {

    @Override
    public void walk(PropertiesNodeListener listener,
                     PropertiesNode propertiesNode) throws SvcLogicException {
        listener.start(propertiesNode);
        walkChildNode(listener, propertiesNode);
        listener.end(propertiesNode);
    }

    /**
     * Walks the children node from the parent node.
     *
     * @param listener properties node listener
     * @param propertiesNode properties node
     * @throws SvcLogicException when properties node walking fails
     */
    public void walkChildNode(PropertiesNodeListener listener,
                              PropertiesNode propertiesNode)
            throws SvcLogicException {
        Map<String, T> children = getChildren(propertiesNode);
        if (children != null) {
            for (Map.Entry<String, T> entry : children.entrySet()) {
                PropertiesNode node = ((PropertiesNode) entry.getValue());
                listener.enterPropertiesNode(node);
                walkChildNode(listener, node);
                listener.exitPropertiesNode(node);
            }
        }
    }

    /**
     * Returns the children node according to the property node type.
     *
     * @param value property node
     * @return property node children
     */
    private Map<String,T> getChildren(PropertiesNode value) {
        if (value instanceof RootNode) {
            return ((RootNode) value).children();
        }
        switch (value.nodeType()) {
            case SINGLE_INSTANCE_NODE:
                return ((InnerNode) value).children();
            case MULTI_INSTANCE_HOLDER_NODE:
                return ((Map<String, T>) ((ListHolderNode) value).children());
            case MULTI_INSTANCE_NODE:
                return ((Map<String, T>) ((MultiInstanceNode) value)
                        .children());
            case MULTI_INSTANCE_LEAF_HOLDER_NODE:
                return ((Map<String, T>) ((LeafListHolderNode) value)
                        .children());
            case SINGLE_INSTANCE_LEAF_NODE:
            case MULTI_INSTANCE_LEAF_NODE:
                return null;
            default:
                throw new IllegalArgumentException("No more types allowed");
        }
    }
}
