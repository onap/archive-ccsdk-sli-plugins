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

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.restapicall.RestapiCallNode;
import org.onap.ccsdk.sli.plugins.restconfapicall.RestconfApiCallNode;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestRestconfDiscoveryNode {

    private static final URI CONTEXT = URI.create("http://localhost:8080/");

    @Test
    public void testEstablishPersistentConnection() throws SvcLogicException,
            InterruptedException {
        final ResourceConfig resourceConfig = new ResourceConfig(
                SseServerMock.class, SseFeature.class);
        GrizzlyHttpServerFactory.createHttpServer(CONTEXT, resourceConfig);
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("prop.encoding-json", "encoding-json");
        ctx.setAttribute("restapi-result.response-code", "200");
        ctx.setAttribute("restapi-result.ietf-subscribed-notifications" +
                                 ":establish-subscription.output.identifier",
                         "100");

        Map<String, String> p = new HashMap<>();
        p.put("sseConnectURL", "http://localhost:8080/events");
        p.put("subscriberId", "networkId");
        p.put("responsePrefix", "restapi-result");
        RestconfDiscoveryNode rdn = new RestconfDiscoveryNode(
                new RestconfApiCallNode(new RestapiCallNode()));
        rdn.establishPersistentConnection(p, ctx, "networkId");
        Thread.sleep(2000);
        rdn.deleteSubscription(p, ctx);
    }

    @Test(expected = SvcLogicException.class)
    public void testSubGraphExecution() throws SvcLogicException{
        SvcLogicGraphInfo subDg = new SvcLogicGraphInfo();
        subDg.mode("sync");
        subDg.module("l3VpnService");
        subDg.rpc("createVpn");
        subDg.version("1.0");
        SvcLogicContext ctx = new SvcLogicContext();
        subDg.executeGraph(ctx);
    }

    @Test(expected = SvcLogicException.class)
    public void testEstablishSubscriptionWithoutSubscriberId()
            throws SvcLogicException{
        SvcLogicContext ctx = new SvcLogicContext();
        Map<String, String> p = new HashMap<>();
        RestconfDiscoveryNode rdn = new RestconfDiscoveryNode(
                new RestconfApiCallNode(new RestapiCallNode()));
        rdn.establishSubscription(p, ctx);
    }

    @Test
    public void testResponseCode() {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("restapi-result.response-code", "200");
        ctx.setAttribute("response-code", "404");
        RestconfDiscoveryNode rdn = new RestconfDiscoveryNode(
                new RestconfApiCallNode(new RestapiCallNode()));
        assertThat(rdn.getResponseCode("restapi-result", ctx),
                   is("200"));
        assertThat(rdn.getResponseCode(null, ctx),
                   is("404"));
    }

    @Test
    public void testOutputIdentifier() {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("restapi-result.ietf-subscribed-notifications:" +
                                 "establish-subscription.output.identifier",
                         "89");
        ctx.setAttribute("ietf-subscribed-notifications:establish-subscripti" +
                                 "on.output.identifier", "89");
        RestconfDiscoveryNode rdn = new RestconfDiscoveryNode(
                new RestconfApiCallNode(new RestapiCallNode()));
        assertThat(rdn.getOutputIdentifier("restapi-result", ctx),
                   is("89"));
    }
}
