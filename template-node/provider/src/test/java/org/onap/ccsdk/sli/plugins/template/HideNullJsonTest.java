package org.onap.ccsdk.sli.plugins.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;

public class HideNullJsonTest {

    @Test
    public void testSampleTemplate() throws Exception {
        TemplateNode t = new MockTemplateNode();

        Map<String, String> params = new HashMap<String, String>();
        params.put(TemplateNode.PREFIX_KEY, "output");
        params.put(TemplateNode.OUTPUT_PATH_KEY, "mycontainer");
        params.put(TemplateNode.TEMPLATE_PATH, "src/test/resources/HideNullJson.vtl");
        
        //Setup sample data to feed into the directive
        params.put("service-type", "\"VPN\""); //the value is quoted to test an override
        params.put("svc-request-id", "REQ001");
        params.put("svc-action", "CREATE");
        params.put("service-instance-id", "SVC001");
        params.put("customerNameTag", "customer-name");
        params.put("customer-name", "TestCust");
        params.put("siidTag", "\"service-instance-id\""); //the value is quoted to test an override

        SvcLogicContext ctx = new SvcLogicContext();
        t.evaluateTemplate(params, ctx);
        String result = ctx.getAttribute("output.mycontainer");
        assertTrue(result.contains("\"svc-request-id\":\"REQ001\","));
        assertTrue(result.contains("\"svc-action\":\"CREATE\""));
        assertFalse(result.contains("\"svc-action\":\"CREATE\",")); // there should be no trailing comma
        assertTrue(result.contains("\"service-type\":\"VPN\","));
        assertTrue(result.contains("\"customer-name\":\"TestCust\","));
        assertTrue(result.contains("\"service-instance-id\":\"SVC001\""));
        assertFalse(result.contains("\"service-instance-id\":\"SVC001\",")); // there should be no trailing comma
        //This should be hidden by the directive because the parameter was never populated
        assertFalse(result.contains("customer-phone-number"));
    }

}