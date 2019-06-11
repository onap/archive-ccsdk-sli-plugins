package org.onap.ccsdk.sli.plugins;

import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HostnameNodeTest {

    @Test
    public void getHostname() {
        Map<String, String> params = new HashMap<>();
        SvcLogicContext ctx = new SvcLogicContext();

        params.put("var-name", "hostname");

        HostnameNode hostnameNode = new HostnameNode();
        try {
            hostnameNode.getHostname(params, ctx);
        } catch (SvcLogicException e) {
            fail("Couldn't get host name");
        }

        System.out.println("Hostname returned "+ctx.getAttribute("hostname"));


    }
}