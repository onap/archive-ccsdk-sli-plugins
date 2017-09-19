package jtest.org.onap.ccsdk.sli.plugins.fabricdiscovery;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.plugins.fabricdiscovery.FabricDiscoveryPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by arun on 9/18/17.
 */

public class TestFabricDiscoveryPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(TestFabricDiscoveryPlugin.class);
    private static final String C_STREAM =
        "ws://localhost:8185/data-change-event-subscription/network-topology:network-topology/datastore=CONFIGURATION/scope=BASE";
    private final String FB_DISCOVERY_STATUS = "fb-response";

    @Test
    public void connectToNotificationServerSuccess() throws Exception {
        SvcLogicContext ctx = new SvcLogicContext();
        String stream = C_STREAM;

        Map<String, String> p = new HashMap<String, String>();
        p.put("stream", stream);
        p.put("enable", "true");

        FabricDiscoveryPlugin fdp = new FabricDiscoveryPlugin();
        fdp.processDcNotificationStream(p, ctx);
        Assert.assertEquals("Success", ctx.getAttribute(FB_DISCOVERY_STATUS));
    }
}
