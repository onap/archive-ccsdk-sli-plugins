package jtest.org.onap.ccsdk.sli.plugins.sshapicall;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.sshapicall.SshApiCallNode;
import org.onap.ccsdk.sli.plugins.sshapicall.model.XmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestSshApiCallNode {

    private static final Logger log = LoggerFactory.getLogger(TestSshApiCallNode.class);

    private SshApiCallNode adapter;
    private String TestId;
    private boolean testMode = true;
    private Map<String, String> params;
    private SvcLogicContext svcContext;


    @Before
    public void setup() throws IllegalArgumentException {
        testMode = true;
        svcContext = new SvcLogicContext();
        adapter = new SshApiCallNode();

        params = new HashMap<>();
        params.put("AgentUrl", "https://192.168.1.1");
        params.put("User", "test");
        params.put("Password", "test");
    }

    @After
    public void tearDown() {
        testMode = false;
        adapter = null;
        params = null;
        svcContext = null;
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommand_noUrlFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("HostName", "test");
        params.put("Port", "10");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execCommand(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandPty_noUrlFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("HostName", "test");
        params.put("Port", "10");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execCommandWithPty(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandResponse_noUrlFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("HostName", "test");
        params.put("Port", "10");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execWithStatusCheck(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommand_noPortFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execCommand(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandPty_noPortFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execCommandWithPty(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandResponse_noPortFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("Test", "fail");
        adapter.execWithStatusCheck(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommand_noCmdFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("Port", "10");
        adapter.execCommand(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandPty_noCmdFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("Port", "10");
        adapter.execCommandWithPty(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandResponse_noCmdFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("Port", "10");
        adapter.execWithStatusCheck(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandResponse_noSSHBasicFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("Port", "10");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("AuthType", "basic");
        params.put("Cmd", "test");
        adapter.execWithStatusCheck(params, svcContext);
    }

    @Test(expected = SvcLogicException.class)
    public void testExecCommandResponse_noSSHKeyFailed() throws SvcLogicException,
            IllegalStateException, IllegalArgumentException {

        params.put("Url", "test");
        params.put("Port", "10");
        params.put("User", "test");
        params.put("Password", "test");
        params.put("AuthType", "key");
        params.put("Cmd", "test");
        adapter.execWithStatusCheck(params, svcContext);
    }
}
