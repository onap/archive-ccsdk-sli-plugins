/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
 *             reserved.
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FabricDiscoveryPlugin implements SvcLogicJavaPlugin, IFabricDiscoveryService {

    private ExecutorService service;
    private Map<String, WebSocketClient> streamMap;
    private static final Logger LOG = LoggerFactory.getLogger(FabricDiscoveryPlugin.class);
    private static final String STREAM_PREFIX = "ws://";
    private static final String FB_DISCOVERY_STATUS = "fb-response";

    public FabricDiscoveryPlugin() {
        service = Executors.newFixedThreadPool(10);
        streamMap = new ConcurrentHashMap<String, WebSocketClient> ();
    }

    @Override
    public void processDcNotificationStream (Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException {
        boolean enable;
        String stream = parseParam(paramMap, "stream", true, null);
        String prefix = parseParam(paramMap, "contextPrefix", false, null);
        String enableStr = parseParam(paramMap, "enable", true, null);

        // Validate the input parameters
        String pfx = (prefix != null) ? prefix + '.' : "";
        if ("true".equalsIgnoreCase(enableStr)) {
            enable = true;
        } else if ("false".equalsIgnoreCase(enableStr)) {
            enable = false;
        } else {
            ctx.setAttribute(pfx + FB_DISCOVERY_STATUS, "Failure");
            throw new SvcLogicException("Incorrect parameter: enable. Valid values are ['true', 'false']");
        }
        if (!STREAM_PREFIX.equalsIgnoreCase(stream.substring(0, 5))) {
            ctx.setAttribute(pfx + FB_DISCOVERY_STATUS, "Failure");
            throw new SvcLogicException("Incorrect parameter: stream, Input is not a web socket address");
        }

        ctx.setAttribute(pfx + FB_DISCOVERY_STATUS, "Success");
        LOG.info("{} monitoring notification stream: {}", (enable) ? "START" : "STOP", stream);

        try {
            service.execute(new Runnable () {
                public void run () {
                    try {
                        URI uri = new URI(stream);
                        if (enable) {
                            if (streamMap.get(stream) != null) {
                                LOG.info("Notification Stream: {} is already being monitoried", stream);
                                return;
                            }
                            IClientMessageCallback messageCallback = new ClientMessageCallback();
                            WebSocketClient wcClient = new WebSocketClient(uri, messageCallback);
                            streamMap.put(stream, wcClient);
                            wcClient.initialize();
                            try {
                                wcClient.connect();
                            } catch (InterruptedException e) {
                                LOG.info("Web Socket Client throws Exception: ", e.getMessage());
                            }
                        } else {
                            WebSocketClient wc = streamMap.get(stream);
                            if (wc != null) {
                                try {
                                    wc.close("Closing");
                                } catch (InterruptedException e) {
                                    LOG.info("Web Socket Client throws Exception: ", e.getMessage());
                                }
                            }
                        }
                    } catch (URISyntaxException e) {
                        LOG.info("Exception converting stream to URI with: ", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            LOG.info("Web Socket client connection throws an exception: ", e.getMessage());
        }
    }

    private String parseParam(Map<String, String> paramMap, String name, boolean required, String def)
        throws SvcLogicException {
        String s = paramMap.get(name);

        if (s == null || s.trim().length() == 0) {
            if (!required)
                return def;
            throw new SvcLogicException("Parameter " + name + " is required in PropertiesNode");
        }

        s = s.trim();
        String value = "";
        int i = 0;
        int i1 = s.indexOf('%');
        while (i1 >= 0) {
            int i2 = s.indexOf('%', i1 + 1);
            if (i2 < 0)
                throw new SvcLogicException("Cannot parse parameter " + name + ": " + s + ": no matching %");

            String varName = s.substring(i1 + 1, i2);
            String varValue = System.getenv(varName);
            if (varValue == null)
                varValue = "";

            value = (new StringBuilder()).append(value)
                    .append(s.substring(i, i1))
                    .append(varValue).toString();
            i = i2 + 1;
            i1 = s.indexOf('%', i);
        }
        value = (new StringBuilder()).append(value)
                     .append(s.substring(i)).toString();

        LOG.info("Parameter {}: {}", name, value);
        return value;
    }
}
