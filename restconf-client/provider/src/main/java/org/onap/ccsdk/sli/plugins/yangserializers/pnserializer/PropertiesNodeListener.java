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

/**
 * Abstraction of properties node listener.
 */
public interface PropertiesNodeListener {

    /**
     * Pre-configurations required before starting the walking.
     *
     * @param node properties node
     */
    void start(PropertiesNode node);

    /**
     * Post-configurations required after starting the walking.
     *
     * @param node properties node
     */
    void end(PropertiesNode node);

    /**
     * Enters the properties node.
     *
     * @param node properties node
     */
    void enterPropertiesNode(PropertiesNode node);

    /**
     * Enters the properties node.
     *
     * @param node properties node
     */
    void exitPropertiesNode(PropertiesNode node);
}
