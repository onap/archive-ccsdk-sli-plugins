package org.onap.ccsdk.sli.plugins.prop.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.restapicall.RestapiCallNode;

public class RestApiCallNodeModule extends AbstractLightyModule implements LightyModule {

    private RestapiCallNode restapiCallNode;

    @Override
    protected boolean initProcedure() {
        this.restapiCallNode = new RestapiCallNode();
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }

    public RestapiCallNode getPropertiesNode() {
        return this.restapiCallNode;
    }

}
