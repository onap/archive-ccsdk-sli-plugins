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
import org.opendaylight.yangtools.yang.model.api.SchemaNode;

/**
 * Representation of MDSAL based schema helper.
 */
public class MdsalSerializerHelper extends SerializerHelper<SchemaNode> {

    protected MdsalSerializerHelper(SchemaNode node, String uri) {
        super(node, uri);
    }

    @Override
    public SchemaNode getRootContext() {
        return null;
    }

    @Override
    public SchemaNode getCurContext() {
        return null;
    }

    @Override
    public SchemaNode getChildContext(SchemaNode schemaNode, String name, String namespace) {
        return null;
    }

    @Override
    public NodeType getNodeType(SchemaNode schemaNode) {
        return null;
    }

    @Override
    public void addNode(String name, String namespace, String value, String valNamespace, NodeType type) {
    }

    @Override
    public void exitNode() {
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }
}
