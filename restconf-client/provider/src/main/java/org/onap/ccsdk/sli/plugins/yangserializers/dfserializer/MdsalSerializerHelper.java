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

import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.Namespace;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.PropertiesNode;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.RootNode;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.LeafListSchemaNode;
import org.opendaylight.yangtools.yang.model.api.LeafSchemaNode;
import org.opendaylight.yangtools.yang.model.api.ListSchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.TypeDefinition;
import org.opendaylight.yangtools.yang.model.api.type.IdentityrefTypeDefinition;

import java.util.Deque;

import static java.lang.String.format;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.DF_ERR;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.NODE_TYPE_ERR;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.PROP_NODE_ERR;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.getResolvedNamespace;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.resolveBaseTypeFrom;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.MdsalPropertiesNodeUtils.getRevision;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_HOLDER_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_LEAF_HOLDER_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_NODE;
import static org.opendaylight.yangtools.yang.data.util.ParserStreamUtils.findSchemaNodeByNameAndNamespace;

/**
 * Representation of MDSAL based serializer helper, which adds properties
 * node to the properties tree based on its types.
 */
public class MdsalSerializerHelper extends SerializerHelper<SchemaNode, SchemaContext> {

    /**
     * Current properties node.
     */
    private PropertiesNode propNode;

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
        Namespace ns = new Namespace(n.getQName().getLocalName(),
                                     n.getQName().getNamespace(),
                                     getRevision(n.getQName().getRevision()));
        propNode = new RootNode<>(n.getQName().getLocalName(), ns,
                                  getSchemaNode(), u);
        curSchemaNode = getSchemaNode();
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
                           String valNameSpace, NodeType type)
            throws SvcLogicException {
        Namespace ns;
        if (type == null) {
            ns = getResolvedNamespace(null, curSchemaNode, getSchemaCtx(),
                                      nameSpace, propNode);
        } else {
            ns = getResolvedNamespace(nameSpace, curSchemaNode, getSchemaCtx(),
                                      nameSpace, propNode);
        }
        if (isChildPresent(name, ns)) {
            addNodeToProperty(name, ns, value, valNameSpace, type);
        }
    }

    @Override
    protected void exitNode() throws SvcLogicException {
        propNode = propNode.parent();
        if (propNode != null) {
            NodeType type = propNode.nodeType();
            if (type == MULTI_INSTANCE_HOLDER_NODE ||
                    type == MULTI_INSTANCE_LEAF_HOLDER_NODE) {
                propNode = propNode.parent();
            }
        }
        if (propNode == null || propNode.appInfo() == null
                || !(propNode.appInfo() instanceof SchemaNode)) {
            throw new SvcLogicException(PROP_NODE_ERR);
        }
        curSchemaNode = (SchemaNode) propNode.appInfo();
    }

    @Override
    protected PropertiesNode getPropertiesNode() {
        return propNode;
    }

    /**
     * Adds the node to property node based on the type of the schema node,
     * which is decided based on the name and namespace of the input
     * information.
     *
     * @param name         name of the node
     * @param ns           namespace of the node
     * @param value        value of the node if its a leaf/leaf-list
     * @param valNamespace namespace of the value
     * @param type         type of the node
     * @throws SvcLogicException when adding child fails
     */
    private void addNodeToProperty(String name, Namespace ns, String value,
                                   String valNamespace, NodeType type)
            throws SvcLogicException {
        Namespace valueNs;
        if (type != null) {
            validateNodeType(type);
        }
        if (curSchemaNode instanceof LeafSchemaNode) {
            valueNs = getValueNs(curSchemaNode, valNamespace, type);
            propNode = propNode.addChild(name, ns,
                                         SINGLE_INSTANCE_LEAF_NODE,
                                         value, valueNs, curSchemaNode);
        } else if (curSchemaNode instanceof LeafListSchemaNode) {
            valueNs = getValueNs(curSchemaNode, valNamespace, type);
            propNode = propNode.addChild(null, name, ns,
                                         MULTI_INSTANCE_LEAF_NODE, value,
                                         valueNs, curSchemaNode);
        } else if (curSchemaNode instanceof ListSchemaNode) {
            propNode = propNode.addChild(null, name, ns, MULTI_INSTANCE_NODE,
                                         curSchemaNode);
        } else {
            propNode = propNode.addChild(name, ns, SINGLE_INSTANCE_NODE,
                                         curSchemaNode);
        }
    }

    /**
     * Returns the namespace of the value namespace in case of identity ref.
     *
     * @param schemaNode schema node
     * @param valNs      value name space
     * @param nodeType   node type
     * @return namespace of value namespace
     * @throws SvcLogicException when namespace resolution fails for identityref
     */
    private Namespace getValueNs(SchemaNode schemaNode, String valNs,
                                 NodeType nodeType) throws SvcLogicException {
        Namespace ns = null;
        if (valNs != null) {
            TypeDefinition type = ((LeafSchemaNode) schemaNode).getType();
            TypeDefinition<?> baseType = resolveBaseTypeFrom(type);
            if (baseType instanceof IdentityrefTypeDefinition) {
                if (nodeType == null) {
                    ns = getResolvedNamespace(null,schemaNode, getSchemaCtx(),
                                              valNs, propNode);
                } else {
                    ns = getResolvedNamespace(valNs, schemaNode, getSchemaCtx(),
                                              null, propNode);
                }
            }
        }
        return ns;
    }

    /**
     * Validates that the node type from the data format matches with that of
     * the corresponding schema node.
     *
     * @param type node type from the abstract data format
     * @throws SvcLogicException when the node type is wrong
     */
    private void validateNodeType(NodeType type) throws SvcLogicException {
        boolean verify;
        switch (type) {
            case SINGLE_INSTANCE_LEAF_NODE:
                verify = curSchemaNode instanceof LeafSchemaNode;
                break;

            case MULTI_INSTANCE_LEAF_NODE:
                verify = curSchemaNode instanceof LeafListSchemaNode;
                break;

            case MULTI_INSTANCE_NODE:
                verify = curSchemaNode instanceof ListSchemaNode;
                break;

            case SINGLE_INSTANCE_NODE:
                verify = (!(curSchemaNode instanceof LeafSchemaNode) &&
                        !(curSchemaNode instanceof LeafListSchemaNode) &&
                        !(curSchemaNode instanceof ListSchemaNode));
                break;

            default:
                throw new SvcLogicException(format(NODE_TYPE_ERR,
                                                   type.toString()));
        }
        if (!verify) {
            throw new SvcLogicException(format(DF_ERR, curSchemaNode
                    .getQName().getLocalName(), type.toString()));
        }
    }

    /**
     * Returns true if the child schema is present with the name and
     * namespace inside the current schema node, if present updates the
     * current schema node; false otherwise.
     *
     * @param name      name of the child schema node
     * @param namespace namespace of the child schema node
     * @return returns true if the child schema is available; false otherwise
     */
    private boolean isChildPresent(String name, Namespace namespace) {
        Deque<DataSchemaNode> dataSchema = findSchemaNodeByNameAndNamespace(
                (DataSchemaNode) curSchemaNode, name, namespace.moduleNs());
        if (dataSchema != null) {
            DataSchemaNode node = dataSchema.pop();
            if (node != null) {
                curSchemaNode = node;
                return true;
            }
        }
        return false;
    }
}
