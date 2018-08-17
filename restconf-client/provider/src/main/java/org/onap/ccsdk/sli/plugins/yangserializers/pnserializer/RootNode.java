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

import java.util.HashMap;
import java.util.Map;

/**
 * Abstraction of node representing properties data tree.
 */
public class RootNode<T extends NodeChild> extends PropertiesNode {

    private Map<String, T> children = new HashMap<String, T>();

    protected RootNode(String name, Namespace namespace,
                       Object appInfo, String uri) {
        super(name, namespace, uri, null, appInfo, null);
    }

    /**
     * Returns children.
     *
     * @return children
     */
    public Map<String, T> children() {
        return children;
    }

    /**
     * Sets children.
     *
     * @param children child nodes
     */
    public void children(Map<String, T> children) {
        this.children = children;
    }

    @Override
    public PropertiesNode addChild(String name, Namespace namespace,
                                   NodeType type,
                                   Object appInfo) throws SvcLogicException {
        // TODO : to be implemented
        return null;
    }

    @Override
    public PropertiesNode addChild(String name, Namespace namespace,
                                   NodeType type, String value,
                                   Namespace valuens,
                                   Object appInfo) throws SvcLogicException {
        // TODO : to be implemented
        return null;
    }

    @Override
    public PropertiesNode addChild(String index, String name,
                                   Namespace namespace, NodeType type,
                                   Object appInfo) throws SvcLogicException {
        // TODO : to be implemented
        return null;
    }

    @Override
    public PropertiesNode addChild(String index, String name,
                                   Namespace namespace, NodeType type,
                                   String value, Namespace valueNs,
                                   Object appInfo) throws SvcLogicException {
        // TODO : to be implemented
        return null;
    }
}
