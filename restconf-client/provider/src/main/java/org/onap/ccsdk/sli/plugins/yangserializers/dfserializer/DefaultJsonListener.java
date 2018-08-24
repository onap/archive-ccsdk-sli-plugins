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

import com.fasterxml.jackson.databind.JsonNode;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType;

import static java.lang.String.format;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.NODE_TYPE_ERR;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.MULTI_INSTANCE_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_LEAF_NODE;
import static org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.NodeType.SINGLE_INSTANCE_NODE;


/**
 * Representation of default implementation of JSON listener.
 */
public class DefaultJsonListener implements JsonListener {

    /**
     * Serializer helper to convert to properties node.
     */
    private SerializerHelper serializerHelper;

    /**
     * Name of the current JSON node.
     */
    private String name;

    /**
     * Module name of the current JSON node.
     */
    private String modName;

    /**
     * Creates an instance of default json listener with its serializer helper.
     *
     * @param serializerHelper serializer helper
     */
    public DefaultJsonListener(SerializerHelper serializerHelper) {
        this.serializerHelper = serializerHelper;
    }

    @Override
    public void enterJsonNode(String nodeName, JsonNode node,
                              NodeType nodeType) throws SvcLogicException {
        getNodeName(nodeName);

        switch (nodeType) {
            case SINGLE_INSTANCE_LEAF_NODE:
                serializerHelper.addNode(name, modName, node.asText(), null,
                                         SINGLE_INSTANCE_LEAF_NODE);
                break;

            case MULTI_INSTANCE_LEAF_NODE:
                serializerHelper.addNode(name, modName, node.asText(), null,
                                         MULTI_INSTANCE_LEAF_NODE);
                break;

            case SINGLE_INSTANCE_NODE:
                serializerHelper.addNode(name, modName, null, null,
                                         SINGLE_INSTANCE_NODE);
                break;

            case MULTI_INSTANCE_NODE:
                serializerHelper.addNode(name, modName, null, null,
                                         MULTI_INSTANCE_NODE);
                break;

            default:
                throw new SvcLogicException(format(NODE_TYPE_ERR,
                                                   nodeType.toString()));
        }
    }

    @Override
    public void exitJsonNode(JsonNode node) throws SvcLogicException {
        serializerHelper.exitNode();
    }

    @Override
    public SerializerHelper serializerHelper() {
        return serializerHelper;
    }

    /**
     * Parses the abstract JSON name and fills the node name and node
     * namespace of the current JSON node.
     *
     * @param abstractName abstract JSON name
     */
    private void getNodeName(String abstractName) {
        String[] val = abstractName.split(":");
        if (val.length == 2) {
            modName = val[0];
            name = val[1];
        } else {
            name = val[0];
        }
    }

}
