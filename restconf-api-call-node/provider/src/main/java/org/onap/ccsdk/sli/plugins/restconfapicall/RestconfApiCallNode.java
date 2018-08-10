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

import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.onap.ccsdk.sli.plugins.restapicall.RetryPolicyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Represents the restconf api call node which is used in serializing the
 * properties and sending the XML or JSON request and vice versa.
 */
public class RestconfApiCallNode implements SvcLogicJavaPlugin {

    /**
     * Logger for the restconf api call node class.
     */
    private static final Logger log = LoggerFactory.getLogger(
            RestconfApiCallNode.class);

    /**
     * Retry policy store to give the retry count.
     */
    protected RetryPolicyStore retryPolicyStore;

    /**
     * Returns the retry policy store.
     *
     * @return retry policy store
     */
    protected RetryPolicyStore getRetryPolicyStore() {
        return retryPolicyStore;
    }

    /**
     * Sets the retry policy store.
     *
     * @param retryPolicyStore retry policy store
     */
    public void setRetryPolicyStore(RetryPolicyStore retryPolicyStore) {
        this.retryPolicyStore = retryPolicyStore;
    }

    /**
     * Creates an instance of restconf api call node.
     */
    public RestconfApiCallNode() {
    }

    /**
     * Sends the restconf request using the parameters map and the memory
     * context along with the retry count.
     *
     * @param paramMap           parameters map
     * @param ctx                service logic context
     * @param retryCount         number of retry counts
     */
    public void sendRequest(Map<String, String> paramMap, SvcLogicContext ctx,
                            Integer retryCount) {
        //TODO: Implementation code.
    }

}
