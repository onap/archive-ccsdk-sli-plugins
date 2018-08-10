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

package org.onap.ccsdk.sli.plugins.restconfapicall;

import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;

/**
 * Representation of MDSAL based serializer helper, which adds properties
 * node to the properties tree based on its types.
 */
public class MdsalSerializerHelper extends SerializerHelper<SchemaNode, SchemaContext> {

    /**
     * Current properties node.
     */
    private Object propNode;

    /**
     * Current schema node.
     */
    private SchemaNode curSchemaNode;

    /**
     * Creates MDSAL serializer helper with root schema node, schema context
     * and URI.
     *
     * @param n schema node of the URI's last node
     * @param c schema context
     * @param u URI of the request
     */
    protected MdsalSerializerHelper(SchemaNode n, SchemaContext c,
                                    String u) {
        super(n, c, u);
        //TODO: Implementation code.
    }

    @Override
    protected SchemaNode getSchemaNode() {
        return schemaNode;
    }

    @Override
    protected SchemaContext getSchemaCtx() {
        return schemaCtx;
    }

    @Override
    protected SchemaNode getCurSchema() {
        return curSchemaNode;
    }

    @Override
    protected void addNode(String name, String nameSpace, String value,
                           String valNameSpace, Object type) {
        //TODO: Implementation code.
    }

    @Override
    protected void exitNode() {
        //TODO: Implementation code.
    }

    @Override
    protected Object getPropertiesNode() {
        //TODO: Implementation code.
        return null;
    }
}
