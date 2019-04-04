package org.onap.ccsdk.sli.plugins.prop.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.prop.PropertiesNode;

public class PropertiesNodeModule extends AbstractLightyModule implements LightyModule {

    private final PropertiesNode propertiesNode;

    public PropertiesNodeModule() {
        this.propertiesNode = new PropertiesNode();
    }

    public PropertiesNode getPropertiesNode() {
        return this.propertiesNode;
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