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

package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.List;
import java.util.Map;

/**
 * Abstraction of serializer to encode/decode context memory parameters
 * to/from specified data format.
 */
public abstract class DataFormatSerializer {

    private DataFormat dataFormat;
    private DataFormatSerializerContext serializerContext;

    /**
     * Creates an instance of data format serializer.
     *
     * @param dataFormat type of data format
     * @param serializerContext data format serializer context
     */
    protected DataFormatSerializer(DataFormat dataFormat,
        DataFormatSerializerContext serializerContext) {
        this.dataFormat = dataFormat;
        this.serializerContext = serializerContext;
    }

    /**
     * Encodes context memory parameters to data format.
     *
     * @param param context memory parameter
     * @param annotations annotations
     * @return data format body
     */
    public abstract String encode(Map<String, String> param,
        Map<String, List<Annotation>> annotations);

    /**
     * Decodes data format body to context memory parameters.
     *
     * @param dataFormatBody abstract node
     * @return context memory parameters
     */
    public abstract Map<String, String> decode(String dataFormatBody);

    /**
     * Returns data format serializer context.
     *
     * @return data format serializer context
     */
    public DataFormatSerializerContext serializerContext() {
        return serializerContext;
    }

    /**
     * Returns supported data format.
     *
     * @return supported data format
     */
    public DataFormat dataFormat() {
        return dataFormat;
    }
}