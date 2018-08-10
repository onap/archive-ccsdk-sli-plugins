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

import com.fasterxml.jackson.databind.JsonNode;


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
    public void enterJsonNode(String nodeName, JsonNode node, Object nodeType) {
        //TODO: Implementation code.
    }

    @Override
    public void exitJsonNode(JsonNode node) {
        //TODO: Implementation code.
    }

    @Override
    public SerializerHelper serializerHelper() {
        return serializerHelper;
    }

}
