package org.onap.ccsdk.sli.plugins.sshapicall.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.sshapicall.SshApiCallNode;

public class SshApiCallModule extends AbstractLightyModule implements LightyModule {

    private SshApiCallNode sshApiCallNode;

    @Override
    protected boolean initProcedure() {
        this.sshApiCallNode = new SshApiCallNode();
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }

    public SshApiCallNode getSshApiCallNode() {
        return this.sshApiCallNode;
    }
}
