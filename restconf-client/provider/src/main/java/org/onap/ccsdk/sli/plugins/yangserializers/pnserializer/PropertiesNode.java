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

/**
 * Abstraction of properties node data tree. This intermediate representation
 * will enable data format serializers to be agnostic of DG context memory
 * nuances and thereby will enable faster development of new data faormat
 * serializers.
 */
public abstract class PropertiesNode {

    private String name;
    private String namespace;
    private String uri;
    private PropertiesNode parent;

    /**
     * Creates an instance of properties node.
     */
    protected PropertiesNode() {
    }

    /**
     * Creates an instance of properties node.
     *
     * @param name name of node
     * @param namespace namespace of node, null indicates parent namespace
     * @param uri URI of this node, if null its calculated based on parent and
     * current value of name and namespace
     * @param parent parent's node
     */
    protected PropertiesNode(String name, String namespace, String uri, PropertiesNode parent) {
        this.name = name;
        this.namespace = namespace;
        this.uri = uri;
        this.parent = parent;
    }

    /**
     * Adds a child to a current node.
     *
     * @param name name of child
     * @param namespace namespace of child, null represents parent namespace
     * @param type type of node
     * @return added properties node
     */
    public abstract PropertiesNode addChild(String name, String namespace, NodeType type);

    /**
     * Adds a child with value to a current node.
     *
     * @param name name of child
     * @param namespace namespace of child, null represents parent namespace
     * @param type type of node
     * @param value value of node
     * @return added properties node
     */
    public abstract PropertiesNode addChild(String name, String namespace, NodeType type, String value);

    /**
     * Adds a child at a given index to a current node. To be used in case of
     * leaf holder child's which is multi instance node.
     *
     * @param index index at which node is to be added
     * @param name name of child
     * @param namespace namespace of child, null represents parent namespace
     * @param type type of node
     * @return added properties node
     */
    public abstract PropertiesNode addChild(String index, String name, String namespace, NodeType type);

    public void name(String name) {
        this.name = name;
    }

    public void namespace(String namespace) {
        this.namespace = namespace;
    }

    public void uri(String uri) {
        this.uri = uri;
    }

    public void parent(PropertiesNode parent) {
        this.parent = parent;
    }

    public PropertiesNode parent() {
        return parent;
    }

    public String name() {
        return name;
    }

    public String namespace() {
        return namespace;
    }

    public String uri() {
        return uri;
    }

}

