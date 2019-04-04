package org.onap.ccsdk.sli.plugins.prop.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.prop.PropertiesNode;

public class PropertiesNodeModule extends AbstractLightyModule implements LightyModule {

    private PropertiesNode propertiesNode;

    @Override
    protected boolean initProcedure() {
        this.propertiesNode = new PropertiesNode();
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }

    public PropertiesNode getPropertiesNode() {
        return this.propertiesNode;
    }
}
