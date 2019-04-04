package org.onap.ccsdk.sli.plugins.template.lighty;


import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.template.TemplateNode;

public class TemplateNodeModule extends AbstractLightyModule implements LightyModule {

    private final TemplateNode templateNode;

    public TemplateNodeModule() {
        this.templateNode = new TemplateNode();
    }

    public TemplateNode getTemplateNode() {
        return this.templateNode;
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