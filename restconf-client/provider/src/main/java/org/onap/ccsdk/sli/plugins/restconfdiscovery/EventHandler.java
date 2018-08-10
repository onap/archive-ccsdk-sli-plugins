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

package org.onap.ccsdk.sli.plugins.restconfdiscovery;

import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.onap.ccsdk.sli.plugins.restconfdiscovery.RestconfDiscoveryNode.eventQueue;

/**
 * Listener that can be registered to listen for notifications.
 */
class EventHandler implements EventListener {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onEvent(InboundEvent event) {
        String payload = event.readData();
        if (!eventQueue.offer(payload)) {
            log.error("Unable to process event "
                              + payload + "as processing queue is full");
            throw new RuntimeException("Unable to process event "
                                               + payload
                                               + "as processing queue is full");
        }
    }
}
