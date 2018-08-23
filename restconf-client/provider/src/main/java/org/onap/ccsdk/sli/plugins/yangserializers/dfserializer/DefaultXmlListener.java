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

import org.dom4j.Element;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;

import static java.lang.String.format;
import static org.onap.ccsdk.sli.plugins.yangserializers.dfserializer.DfSerializerUtil.NODE_TYPE_ERR;

/**
 * Representation of default implementation of XML listener.
 */
public class DefaultXmlListener implements XmlListener {

    /**
     * Serializer helper to convert to properties node.
     */
    private SerializerHelper serializerHelper;

    /**
     * Creates an instance of default XML listener with its serializer helper.
     *
     * @param serializerHelper serializer helper
     */
    public DefaultXmlListener(SerializerHelper serializerHelper) {
        this.serializerHelper = serializerHelper;
    }

    @Override
    public void enterXmlElement(Element element, XmlNodeType nodeType)
            throws SvcLogicException {
        switch (nodeType) {
            case TEXT_NODE:
                serializerHelper.addNode(element.getName(),
                                         element.getNamespace().getURI(),
                                         element.getText(), null, null);
                break;

            case OBJECT_NODE:
                serializerHelper.addNode(element.getName(),
                                         element.getNamespace().getURI(),
                                         null, null, null);
                break;

            default:
                throw new SvcLogicException(format(NODE_TYPE_ERR,
                                                   nodeType.toString()));
        }
    }

    @Override
    public void exitXmlElement(Element element) throws SvcLogicException {
        serializerHelper.exitNode();
    }

    @Override
    public SerializerHelper serializerHelper() {
        return serializerHelper;
    }
}
