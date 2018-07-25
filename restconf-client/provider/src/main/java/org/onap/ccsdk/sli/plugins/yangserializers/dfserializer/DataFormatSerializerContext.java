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

import java.util.Map;

/**
 * Abstraction of data format serializer context.
 */
public class DataFormatSerializerContext {

    private Listener listener;
    private String uri;
    private Map<String, String> protocolAnnotation;

    /**
     * Creates an instance of data format serializer context.
     *
     * @param listener data format listener
     * @param uri URI corresponding to instance identifier
     * @param protocolAnnotation protocol annotations
     */
    public DataFormatSerializerContext(Listener listener, String uri,
        Map<String, String> protocolAnnotation) {
        this.listener = listener;
        this.uri = uri;
        this.protocolAnnotation = protocolAnnotation;
    }

    /**
     * Retruns data format listener.
     *
     * @return data format listener
     */
    public Listener listener() {
        return listener;
    }

    /**
     * Returns URI.
     *
     * @return URI
     */
    public String uri() {
        return uri;
    }

    /**
     * Returns protocol annotations.
     *
     * @return protocol annotations
     */
    public Map<String, String> getProtocolAnnotation() {
        return protocolAnnotation;
    }
}