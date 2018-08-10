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

/**
 * Represents the data format listener factory which will return JSON or XML
 * listener according to the serializer helper.
 */
public final class DfListenerFactory {

    /**
     * Returns the instance of the data format listener factory.
     *
     * @return instance of the data format listener factory
     */
    public static DfListenerFactory instance() {
        return DfListenerFactory.LazyHolder.INSTANCE;
    }

    /**
     * Bill pugh singleton pattern. Instance will not be instantiated until
     * the lazy holder class is loaded via a call to the instance of method
     * below.
     */
    private static class LazyHolder {
        private static final DfListenerFactory INSTANCE =
                new DfListenerFactory();
    }

    /**
     * Returns the data format listener by deciding it based on the format of
     * the parameter.
     *
     * @param serHelper serializer helper
     * @param params    parameters
     * @return data format listener
     */
    public Listener getListener(SerializerHelper serHelper,
                                YangParameters params) {
        Listener listener;
        switch (params.format) {
            case JSON:
                listener = new DefaultJsonListener(serHelper);
                break;

            case XML:
                listener = new DefaultXmlListener(serHelper);
                break;

            //TODO: Restconf Exception code to be added.
            default:
                throw new IllegalArgumentException("In correct format");
        }
        return listener;
    }
}
