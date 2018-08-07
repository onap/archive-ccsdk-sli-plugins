package org.onap.ccsdk.sli.plugins.template;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;

public class TemplateNodeTest {

    @Test
    public void sunnyDay() throws Exception {
        String requestId = "REQ001";
        String uniqueKey = "UNIQUE_TEST";
        String action = "uPdaTe";
        String serviceType = "VPN";
        
        TemplateNode t = new MockTemplateNode();

        Map<String, String> params = new HashMap<String, String>();
        params.put(TemplateNode.PREFIX_KEY, "output");
        params.put(TemplateNode.OUTPUT_PATH_KEY, "mycontainer");
        params.put(TemplateNode.TEMPLATE_PATH, "src/test/resources/basic.vtl");
        params.put("service-type", serviceType);
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("input.svc-request-id", requestId);
        ctx.setAttribute("input.unique-key", uniqueKey);
        ctx.setAttribute("action", action);

        t.evaluateTemplate(params, ctx);
        String result = ctx.getAttribute("output.mycontainer");
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains(requestId));
        assertTrue(result.contains(uniqueKey));
        assertTrue(result.contains(action.toUpperCase()));
        assertTrue(result.contains(serviceType));
    }

    @Test(expected = SvcLogicException.class)
    public void parameterException() throws Exception {
        TemplateNode t = new MockTemplateNode();
        Map<String, String> params = new HashMap<String, String>();
        SvcLogicContext ctx = new SvcLogicContext();
        t.evaluateTemplate(params, ctx);
    }

    @Test(expected = SvcLogicException.class)
    public void missingTemplate() throws Exception {
        TemplateNode t = new MockTemplateNode();
        Map<String, String> params = new HashMap<String, String>();
        params.put(TemplateNode.PREFIX_KEY, "output");
        params.put(TemplateNode.OUTPUT_PATH_KEY, "mycontainer");
        params.put(TemplateNode.TEMPLATE_PATH, "src/test/resources/missing.vtl");
        SvcLogicContext ctx = new SvcLogicContext();
        t.evaluateTemplate(params, ctx);
    }

}
