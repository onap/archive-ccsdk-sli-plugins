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

import com.google.gson.stream.JsonWriter;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.PropertiesNode;
import org.onap.ccsdk.sli.plugins.yangserializers.pnserializer.PropertiesNodeListener;

import java.io.Writer;

/**
 * Representation of JSON implementation of properties node listener.
 */
public class PropertiesNodeJsonListener implements PropertiesNodeListener{

    /**
     * JSON writer to write the JSON data format.
     */
    private JsonWriter jsonWriter;

    /**
     * Writer to write the JSON.
     */
    private Writer writer;

    /**
     * Creates the properties node JSON listener by instantiating and
     * indenting the writer.
     */
    public PropertiesNodeJsonListener() {
    }

    @Override
    public void start(PropertiesNode node) {
        //TODO: Implementation code.
    }

    @Override
    public void end(PropertiesNode node) {
        //TODO: Implementation code.
    }

    @Override
    public void enterPropertiesNode(PropertiesNode node) {
        //TODO: Implementation code.
    }

    @Override
    public void exitPropertiesNode(PropertiesNode node) {
        //TODO: Implementation code.
    }
}
