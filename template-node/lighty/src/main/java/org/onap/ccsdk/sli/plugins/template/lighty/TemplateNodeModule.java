package org.onap.ccsdk.sli.plugins.template.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.template.TemplateNode;

public class TemplateNodeModule extends AbstractLightyModule implements LightyModule {

    private TemplateNode templateNode;

    @Override
    protected boolean initProcedure() {
        this.templateNode = new TemplateNode();
        return true;
    }

    @Override
    protected boolean stopProcedure() {
        return true;
    }

    public TemplateNode getTemplateNode() {
        return this.templateNode;
    }
}
