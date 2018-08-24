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
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.util.SchemaContextUtil;

import java.util.Map;

import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getChildSchemaNode;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getIndex;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getListName;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getNamespace;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getNodeType;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getRevision;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getValueNamespace;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.resolveName;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_NODE;

/**
 * Representation of mdsal based properties node serializer implementation.
 */
public class MdsalPropertiesNodeSerializer extends PropertiesNodeSerializer<SchemaNode, SchemaContext> {

    private SchemaNode curSchema;
    private PropertiesNode node;

    /**
     * Creates the properties node serializer.
     *
     * @param schemaNode schema node.
     * @param schemaCtx  schema context
     * @param uri        URL of the request
     */
    public MdsalPropertiesNodeSerializer(SchemaNode schemaNode,
                                         SchemaContext schemaCtx, String uri) {
        super(schemaNode, schemaCtx, uri);
    }

    @Override
    public PropertiesNode encode(Map<String, String> paramMap) throws SvcLogicException {
        curSchema = schemaNode();
        String nodeInUri[] = uri().split("\\/");
        String lastNodeName = nodeInUri[nodeInUri.length - 1];
        String rootUri = uri().replaceAll("\\/", "\\.");
        node = createRootNode(lastNodeName, rootUri);

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String[] names = entry.getKey().split("\\.");
            for (int i = 0; i < names.length; i++) {
                if (i < nodeInUri.length) {
                    if (!(nodeInUri[i].equals(names[i]))) {
                        break;
                    }
                } else {
                    createPropertyNode(i, names.length, names[i],
                                       entry.getValue());
                }
            }
        }
        return node;
    }

    @Override
    public Map<String, String> decode(PropertiesNode propertiesNode)
            throws SvcLogicException {
        PropertiesNodeWalker walker = new DefaultPropertiesNodeWalker<>();
        DefaultPropertiesNodeListener listener = new DefaultPropertiesNodeListener();
        walker.walk(listener, propertiesNode);
        return listener.params();
    }

    private RootNode createRootNode(String lastNodeName, String rootUri) {
        Module m = SchemaContextUtil.findParentModule(schemaCtx(), curSchema);
        Namespace ns = new Namespace(m.getName(), m.getNamespace(),
                                     getRevision(m.getRevision()));
        return new RootNode(lastNodeName, ns, schemaNode(), rootUri);
    }

    private void createPropertyNode(int index, int length, String name,
                                    String value) throws SvcLogicException {
        String localName = resolveName(name);
        Namespace ns = getNamespace(getListName(name), schemaCtx(), node);
        SchemaNode schema = getChildSchemaNode(curSchema, localName, ns);
        if (schema == null) {
            return;
        }

        switch (getNodeType(index, length, name)) {
            case SINGLE_INSTANCE_NODE:
                node = node.addChild(localName, ns,
                                     SINGLE_INSTANCE_NODE, schema);
                curSchema = schema;
                break;
            case MULTI_INSTANCE_NODE:
                node = node.addChild(getIndex(name), localName, ns,
                                     MULTI_INSTANCE_NODE, schema);
                curSchema = schema;
                break;
            case SINGLE_INSTANCE_LEAF_NODE:
                node = node.addChild(localName, ns, SINGLE_INSTANCE_LEAF_NODE,
                                     value, getValueNamespace(value, schemaCtx()),
                                     schema);
                node = node.endNode();
                curSchema = ((SchemaNode) node.appInfo());
                break;
            case MULTI_INSTANCE_LEAF_NODE:
                node = node.addChild(getIndex(name), localName, ns,
                                     MULTI_INSTANCE_LEAF_NODE, value,
                                     getValueNamespace(value, schemaCtx()),
                                     schema);
                node = node.endNode();
                curSchema = ((SchemaNode) node.appInfo());
                break;
            default:
                throw new SvcLogicException("Invalid node type");
        }
    }
}
