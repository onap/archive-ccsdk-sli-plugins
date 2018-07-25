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

package org.onap.ccsdk.sli.plugins.yangserializers.dfserializer;

import java.util.Map;

/**
 * Abstraction of an entity which helps the data format serializers to obtain
 * schema context details and to build properties from data.
 *
 * @param <T> type of schema node
 */
public abstract class SerializerHelper<T> {

    private T rootSchema;
    private String rootURI;

    protected SerializerHelper(T t, String uri) {
        rootSchema = t;
    }

    /**
     * Returns root schema context node.
     *
     * @return root schema context node
     */
    protected abstract T getRootContext();

    /**
     * Returns current schema context node.
     *
     * @return current schema context node
     */
    protected abstract T getCurContext();

    /**
     * Returns child schema context node.
     *
     * @return child schema context node
     */
    protected abstract T getChildContext(T t, String name, String namespace);

    /**
     * Returns type of node
     * @param t node
     * @return node type
     */
    protected abstract NodeType getNodeType(T t);

    /**
     * Adds a node to current tree.
     *
     * @param name name of node
     * @param namespace namespace of node, it can be either module name or
     * namespace, null indicates parent namespace
     * @param value value of node, in case it's leaf/leaf-list node
     * @param valNamespace value namespace for identityref, could be module
     * name or namespace
     * @param type type of node if known like in case of JSON
     */
    protected abstract void addNode(String name, String namespace, String value,
        String valNamespace, NodeType type);

    /**
     * Exits the node, in case if it's leaf node add to properties map.
     */
    protected abstract void exitNode();

    /**
     * Returns the properties built corresponding to data.
     *
     * @return properties map
     */
    protected abstract Map<String, String> getProperties();
}
