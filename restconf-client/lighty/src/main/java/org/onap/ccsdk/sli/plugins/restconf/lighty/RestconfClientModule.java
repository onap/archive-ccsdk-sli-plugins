package org.onap.ccsdk.sli.plugins.restconf.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.restapicall.RestapiCallNode;
import org.onap.ccsdk.sli.plugins.restconfapicall.RestconfApiCallNode;
import org.onap.ccsdk.sli.plugins.restconfdiscovery.RestconfDiscoveryNode;

public class RestconfClientModule extends AbstractLightyModule implements LightyModule {

    private final RestapiCallNode restapiCallNode;

    private RestconfApiCallNode restconfApiCallNode;
    private RestconfDiscoveryNode restconfDiscoveryNode;

    public RestconfClientModule(final RestapiCallNode restapiCallNode) {
        this.restapiCallNode = restapiCallNode;
    }

    @Override
    protected boolean initProcedure() {
        this.restconfApiCallNode = new RestconfApiCallNode(restapiCallNode);
        this.restconfDiscoveryNode = new RestconfDiscoveryNode(restconfApiCallNode);
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }

    public RestconfApiCallNode getRestconfApiCallNode() {
        return this.restconfApiCallNode;
    }

    public RestconfDiscoveryNode getRestconfDiscoveryNode() {
        return this.restconfDiscoveryNode;
    }
}
