package jtest.org.onap.ccsdk.sli.plugins.prop;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.prop.PropertiesNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPropertiesNode {

    private static final Logger log = LoggerFactory.getLogger(TestPropertiesNode.class);
    
    @Test
    public void testJSONFileParsing() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.json");
        p.put("contextPrefix", "test-json");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("test-json.message"),"The provisioned access " +
                "bandwidth is at or exceeds 50% of the total server capacity.");
    }

    @Test
    public void testJSONFileArrayParsing() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.json");
        p.put("contextPrefix", "test-json");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("test-json.equipment-data[0].max-server-speed"),"1600000");
        assertEquals(ctx.getAttribute("test-json.resource-state.used"),"1605000");
        assertEquals(ctx.getAttribute("test-json.resource-rule.service-model"),"DUMMY");
        assertEquals(ctx.getAttribute("test-json.resource-rule.endpoint-position"),"VCE-Cust");
    }

    @Test
    public void testJSONFileParsingPrefixCheck() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.json");
        p.put("contextPrefix", "");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("equipment-data[0].max-server-speed"),"1600000");
        assertEquals(ctx.getAttribute("resource-state.used"),"1605000");
        assertEquals(ctx.getAttribute("resource-rule.service-model"),"DUMMY");
        assertEquals(ctx.getAttribute("resource-rule.endpoint-position"),"VCE-Cust");
        assertEquals(ctx.getAttribute("resource-rule.hard-limit-expression"),"max-server-" +
                "speed * number-primary-servers");
    }

    @Test
    public void testJSONFileParsingNoPrefix() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.json");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("equipment-data[0].max-server-speed"),"1600000");
        assertEquals(ctx.getAttribute("resource-state.used"),"1605000");
        assertEquals(ctx.getAttribute("resource-rule.service-model"),"DUMMY");
        assertEquals(ctx.getAttribute("resource-rule.endpoint-position"),"VCE-Cust");
        assertEquals(ctx.getAttribute("resource-rule.hard-limit-expression"),"max-server-" +
                "speed * number-primary-servers");
    }

    @Test
    public void testJSONFileParsingCtxCheck() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.json");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("equipment-data[0].max-server-speed"),"1600000");
        assertEquals(ctx.getAttribute("resource-state.used"),"1605000");
        assertEquals(ctx.getAttribute("resource-rule.service-model"),"DUMMY");
        assertEquals(ctx.getAttribute("resource-rule.endpoint-position"),"VCE-Cust");
        assertEquals(ctx.getAttribute("resource-rule.hard-limit-expression"),"max-server-" +
                "speed * number-primary-servers");
        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }

    @Test(expected = SvcLogicException.class)
    public void testToPropertiesInvalidJson() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test-invalid.json");
        p.put("contextPrefix", "invalid");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }

    @Test
    public void testTXTFileParsing() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.txt");
        p.put("contextPrefix", "test-txt");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("test-txt.service-data.service-information.service-type"),"AVPN");
        assertEquals(ctx.getAttribute("test-txt.service-configuration-notification-input.response-code"),"0");
        assertEquals(ctx.getAttribute("test-txt.operational-data.avpn-ip-port-information.port-" +
                                              "level-cos.queueing.pe-per-class-queueing-behaviors.cos3-queueing"),"WRED");
        assertEquals(ctx.getAttribute("test-txt.service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.l1-customer-handoff"),"_1000BASELX");
        assertEquals(ctx.getAttribute("test-txt.service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.vlan-tag-control"),"_1Q");
    }

    @Test
    public void testTXTFileParsingPrefixCheck() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.txt");
        p.put("contextPrefix", "");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("service-data.service-information.service-type"),"AVPN");
        assertEquals(ctx.getAttribute("service-configuration-notification-input.response-code"),"0");
        assertEquals(ctx.getAttribute("operational-data.avpn-ip-port-information.port-" +
                                              "level-cos.queueing.pe-per-class-queueing-behaviors.cos3-queueing"),"WRED");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.l1-customer-handoff"),"_1000BASELX");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.vlan-tag-control"),"_1Q");
    }

    @Test
    public void testTXTFileParsingNoPrefix() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.txt");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("service-data.service-information.service-type"),"AVPN");
        assertEquals(ctx.getAttribute("service-configuration-notification-input.response-code"),"0");
        assertEquals(ctx.getAttribute("operational-data.avpn-ip-port-information.port-" +
                                              "level-cos.queueing.pe-per-class-queueing-behaviors.cos3-queueing"),"WRED");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.l1-customer-handoff"),"_1000BASELX");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.vlan-tag-control"),"_1Q");
    }

    @Test
    public void testTXTFileParsingCtxCheck() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/test/resources/test.txt");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("service-data.service-information.service-type"),"AVPN");
        assertEquals(ctx.getAttribute("service-configuration-notification-input.response-code"),"0");
        assertEquals(ctx.getAttribute("operational-data.avpn-ip-port-information.port-" +
                                              "level-cos.queueing.pe-per-class-queueing-behaviors.cos3-queueing"),"WRED");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.l1-customer-handoff"),"_1000BASELX");
        assertEquals(ctx.getAttribute("service-data.avpn-ip-port-information.avpn-" +
                                              "access-information.vlan-tag-control"),"_1Q");
        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }

    @Test(expected = SvcLogicException.class)
    public void testToPropertiesInvalidParam() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();
        p.put("responsePrefix", "response");
        p.put("skipSending", "true");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }

    @Test(expected = SvcLogicException.class)
    public void testToPropertiesNoParam() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }

    @Test(expected = SvcLogicException.class)
    public void testToPropertiesFilePathError() throws SvcLogicException {
        SvcLogicContext ctx = new SvcLogicContext();
        ctx.setAttribute("tmp.sdn-circuit-req-row_length", "1");

        Map<String, String> p = new HashMap<String, String>();
        p.put("fileName", "src/tests/resources/test.txt");

        PropertiesNode rcn = new PropertiesNode();
        rcn.readProperties(p, ctx);

        assertEquals(ctx.getAttribute("tmp.sdn-circuit-req-row_length"),"1");
    }
}
