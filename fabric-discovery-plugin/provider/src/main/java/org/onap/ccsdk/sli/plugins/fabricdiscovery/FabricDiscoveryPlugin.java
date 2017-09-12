/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
 * 			reserved.
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

package org.onap.ccsdk.sli.plugins.fabricdiscovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.Map;
import java.util.HashMap;

import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FabricDiscoveryPlugin implements SvcLogicJavaPlugin, IFabricDiscoveryService {

    private ExecutorService service;
    private Map<String, WebSocketClient> streamMap;
    private static final Logger LOG = LoggerFactory.getLogger(FabricDiscoveryPlugin.class);

    public FabricDiscoveryPlugin() {
        service = Executors.newFixedThreadPool(10);
        streamMap = new HashMap<String, WebSocketClient> ();
    }

    @Override
    public void processDcNotificationStream (String stream, boolean enable) {
        if (enable) {
            LOG.info("Starting Notification Monitoring of stream: " + stream);
        } else {
            LOG.info("Stopping Notification  Monitoring of stream: " + stream);
        }

        NotificationProcessor np = new NotificationProcessor(stream, enable);
        service.execute(np);
    }

    private class NotificationProcessor implements Runnable {
        private String stream;
        private boolean enable;
        URI uri = null;


        public NotificationProcessor (String stream, boolean enable) {
            this.stream = stream;
            this.enable = enable;
        }
        @Override
        public void run () {
            try {
                uri = new URI(stream);
            } catch (URISyntaxException e) {
            } finally {
                if (enable) {
                    IClientMessageCallback messageCallback = new ClientMessageCallback();
                    WebSocketClient wcClient = new WebSocketClient(uri, messageCallback);
                    streamMap.put(stream, wcClient);
                    wcClient.initialize();
                    try {
                        wcClient.connect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    WebSocketClient wc = streamMap.get(stream);
                    if (wc != null) {
                        try {
                            wc.close("Closing");
                        } catch (InterruptedException e) {
                            e.printStackTrace ();
                        }
                    }
                }
            }
        }
    }
}
