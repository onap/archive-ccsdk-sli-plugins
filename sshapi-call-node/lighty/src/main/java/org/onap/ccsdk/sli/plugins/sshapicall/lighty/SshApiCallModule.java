package org.onap.ccsdk.sli.plugins.sshapicall.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.sshapicall.SshApiCallNode;

public class SshApiCallModule extends AbstractLightyModule implements LightyModule {

    private final SshApiCallNode sshApiCallNode;

    public SshApiCallModule() {
        this.sshApiCallNode = new SshApiCallNode();
    }

    public SshApiCallNode getSshApiCallNode() {
        return this.sshApiCallNode;
    }

    @Override
    protected boolean initProcedure() {
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }
}