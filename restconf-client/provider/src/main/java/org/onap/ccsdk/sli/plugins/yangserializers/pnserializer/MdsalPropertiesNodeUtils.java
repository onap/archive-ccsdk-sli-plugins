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

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.data.impl.schema.SchemaUtils;
import org.opendaylight.yangtools.yang.data.util.ParserStreamUtils;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.ChoiceSchemaNode;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

/**
 * Represents utilities for properties node tree.
 */
public final class MdsalPropertiesNodeUtils {

    private MdsalPropertiesNodeUtils() {
    }

    /**
     * Returns the index from multi instance property name.
     *
     * @param name name of the property
     * @return index from multi instance property name
     */
    public static String getIndex(String name) {
        return name.substring(name.indexOf("[") + 1,
                              name.indexOf("]"));
    }

    /**
     * Returns the multi instance property name.
     *
     * @param name name of the property
     * @return the multi instance property name
     */
    public static String getListName(String name) {
        String[] s = name.split("\\[");
        return s[0];
    }

    /**
     * Returns true if property is multi instance.
     *
     * @param name name of the property
     * @return true if property is multi instance
     */
    public static boolean isListEntry(String name) {
        String s[] = name.split("\\[");
        return s.length > 1;
    }

    /**
     * Returns name of the property after pruning namespace and
     * index if the property is multi instance.
     *
     * @param name name of the property
     * @return name of the property
     */
    public static String resolveName(String name) {
        String localName = getListName(name);
        final int lastIndexOfColon = localName.lastIndexOf(":");
        if (lastIndexOfColon != -1) {
            localName = localName.substring(lastIndexOfColon + 1);
        }
        return localName;
    }

    /**
     * Adds current node to parent's augmentation map.
     *
     * @param augSchema augment schema
     * @param parent parent property node
     * @param curNode current property node
     */
    public static void addToAugmentations(AugmentationSchemaNode augSchema,
                                          PropertiesNode parent,
                                          PropertiesNode curNode) {
        Collection<PropertiesNode> childsFromAugmentation = parent
                .augmentations().get(augSchema);
        if (!childsFromAugmentation.isEmpty()) {
            for (PropertiesNode pNode : childsFromAugmentation) {
                if (pNode.name().equals(curNode.name())) {
                    return;
                }
            }
        }
        parent.augmentations().put(augSchema, curNode);
    }


    /**
     * Returns augmented properties node if it is already
     * added in properties tree.
     *
     * @param augSchema augmented schema node
     * @param parent parent properties node
     * @param name name of the properties
     * @return augmented properties node if it is already added
     */
    public static PropertiesNode getAugmentationNode(
            AugmentationSchemaNode augSchema,
            PropertiesNode parent, String name) {
        if (augSchema != null) {
            Collection<PropertiesNode> childsFromAugmentation = parent
                    .augmentations().get(augSchema);
            if (!childsFromAugmentation.isEmpty()) {
                for (PropertiesNode pNode : childsFromAugmentation) {
                    if (pNode.name().equals(name)) {
                        return pNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates uri with specified name and namespace.
     *
     * @param parent parent properties node
     * @param name name of the node
     * @param ns namespace of the node
     * @return uri with specified name and namespace
     */
    public static String getUri(PropertiesNode parent, String name,
                                Namespace ns) {
        String uri = name;
        if (!(parent.namespace().moduleNs().equals(ns.moduleNs()))) {
            uri = ns.moduleName() + ":" + name;
        }
        return parent.uri() + "." + uri;
    }

    /**
     * Creates new properties with specified parameters.
     *
     * @param name name of the properties node
     * @param namespace namespace of the properties node
     * @param uri uri of the properties node
     * @param parent parent node
     * @param appInfo application info
     * @param type node type
     * @return new properties node
     */
    public static PropertiesNode createNode(String name, Namespace namespace,
                                            String uri, PropertiesNode parent,
                                            Object appInfo, NodeType type) {
        switch (type) {
            case SINGLE_INSTANCE_NODE:
                return new SingleInstanceNode(name, namespace, uri, parent, appInfo, type);
            case MULTI_INSTANCE_HOLDER_NODE:
                return new ListHolderNode(name, namespace, uri, parent, appInfo, type);
            case MULTI_INSTANCE_LEAF_HOLDER_NODE:
                return new LeafListHolderNode(name, namespace, uri, parent, appInfo, type);
            default:
                throw new RuntimeException("Invalid node type");
        }
    }

    /**
     * Returns true if namespace is same as parent's namespace.
     *
     * @param parent parent property node
     * @param curNode current property node
     * @return true if namespace is same as parent namespace
     */
    public static boolean isNamespaceAsParent(PropertiesNode parent,
                                              PropertiesNode curNode) {
        return parent.namespace().moduleNs().equals(curNode.namespace().moduleNs());
    }

    /**
     * Returns namespace.
     *
     * @param childName name of the property
     * @param ctx schema context
     * @param parent parent property node
     * @return namespace
     */
    public static Namespace getNamespace(String childName,
                                         SchemaContext ctx,
                                         PropertiesNode parent) {
        int lastIndexOfColon = childName.lastIndexOf(":");
        if (lastIndexOfColon != -1) {
            String moduleName = childName.substring(0, lastIndexOfColon);
            Iterator<Module> it = ctx.findModules(moduleName).iterator();
            if (!it.hasNext()) {
                // module is not present in context
                return null;
            }
            Module m = it.next();
            return new Namespace(moduleName, m.getQNameModule().getNamespace(),
                                 getRevision(m.getRevision()));
        }
        Namespace parentNs = parent.namespace();
        return new Namespace(parentNs.moduleName(), parentNs.moduleNs(),
                             parentNs.revision());
    }

    /**
     * Returns child schema node.
     *
     * @param curSchema current schema node
     * @param name name of the property
     * @param namespace namespace of the property
     * @return child schema node
     */
    public static SchemaNode getChildSchemaNode(SchemaNode curSchema,
                                                     String name,
                                                Namespace namespace) {
        if (namespace == null) {
            return null;
        }

        QName qname =  QName.create(namespace.moduleNs(),
                                    Revision.of(namespace.revision()), name);

        // YANG RPC will not be instance of DataSchemaNode
        if (curSchema instanceof DataSchemaNode) {
            Deque<DataSchemaNode> schemaNodeDeque = ParserStreamUtils.
                    findSchemaNodeByNameAndNamespace(((DataSchemaNode)
                            curSchema), name, namespace.moduleNs());
            if (schemaNodeDeque.isEmpty()) {
                // could not find schema node
                return null;
            }

            DataSchemaNode schemaNode = schemaNodeDeque.pop();
            if (schemaNodeDeque.isEmpty()){
                // Simple node
                return schemaNode;
            }

            // node is child of Choice/case
            return SchemaUtils.findSchemaForChild(((ChoiceSchemaNode) schemaNode),
                                                  qname);
        } else {
            return SchemaUtils.findDataChildSchemaByQName(curSchema, qname);
        }
    }

    /**
     * Returns the property node type.
     *
     * @param index current index
     * @param length length of the properties
     * @param name name of the property
     * @return the property node type
     */
    public static NodeType getNodeType(int index, int length, String name) {
        if (index == length-1) {
            return (isListEntry(name) ? NodeType.MULTI_INSTANCE_LEAF_NODE :
                    NodeType.SINGLE_INSTANCE_LEAF_NODE);
        } else {
            return (isListEntry(name) ? NodeType.MULTI_INSTANCE_NODE :
                    NodeType.SINGLE_INSTANCE_NODE);
        }
    }

    /**
     * Returns revision in string.
     *
     * @param r YANG revision
     * @return revision in string
     */
    public static String getRevision(Optional<Revision> r) {
        return (r.isPresent()) ? r.get().toString() : null;
    }
}
