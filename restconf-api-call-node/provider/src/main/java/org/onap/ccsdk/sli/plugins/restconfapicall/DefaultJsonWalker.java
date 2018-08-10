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
 * Implementation of JSON walker to walk through the nodes and process it.
 */
public class DefaultJsonWalker implements JsonWalker {

    @Override
    public void walk(JsonListener listener, JsonNode jsonNode) {
        //TODO: Implementation code.
    }

    /**
     * Processes single instance node or leaf, by adding the node to from
     * JSON and walking through all its children recursively.
     *
     * @param key      JSON name
     * @param value    JSON node
     * @param listener JSON listener
     */
    private void processSingleNode(String key, JsonNode value,
                                   JsonListener listener) {
        //TODO: Implementation code.
    }

    /**
     * Processes multi instance node or leaf, by adding the node to from JSON
     * and walking through all its instance recursively.
     *
     * @param key      JSON name
     * @param value    JSON node
     * @param listener JSON listener
     */
    private void processMultiNodes(String key, JsonNode value,
                                   JsonListener listener) {
        //TODO: Implementation code.
    }
}
