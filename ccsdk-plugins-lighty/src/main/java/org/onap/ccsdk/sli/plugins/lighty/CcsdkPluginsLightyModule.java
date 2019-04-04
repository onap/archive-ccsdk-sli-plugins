package org.onap.ccsdk.sli.plugins.lighty;

import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import java.util.concurrent.ExecutionException;
import org.onap.ccsdk.sli.plugins.prop.lighty.PropertiesNodeModule;
import org.onap.ccsdk.sli.plugins.prop.lighty.RestApiCallNodeModule;
import org.onap.ccsdk.sli.plugins.restconf.lighty.RestconfClientModule;
import org.onap.ccsdk.sli.plugins.sshapicall.lighty.SshApiCallModule;
import org.onap.ccsdk.sli.plugins.template.lighty.TemplateNodeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CcsdkPluginsLightyModule extends AbstractLightyModule {

    private static final Logger LOG = LoggerFactory.getLogger(CcsdkPluginsLightyModule.class);

    private PropertiesNodeModule propertiesNodeModule;
    private RestApiCallNodeModule restApiCallNodeModule;
    private RestconfClientModule restconfClientModule;
    private SshApiCallModule sshApiCallModule;
    private TemplateNodeModule templateNodeModule;

    protected boolean initProcedure() {
        LOG.debug("Initializing CCSDK Plugins Lighty module...");

        this.propertiesNodeModule = new PropertiesNodeModule();
        if (!startLightyModule(propertiesNodeModule)) {
            LOG.error("Unable to start PropertiesNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.restApiCallNodeModule = new RestApiCallNodeModule();
        if (!startLightyModule(restApiCallNodeModule)) {
            LOG.error("Unable to start RestApiCallNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.restconfClientModule = new RestconfClientModule(restApiCallNodeModule.getPropertiesNode());
        if (!startLightyModule(restconfClientModule)) {
            LOG.error("Unable to start RestconfClientModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.sshApiCallModule = new SshApiCallModule();
        if (!startLightyModule(sshApiCallModule)) {
            LOG.error("Unable to start SshApiCallModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.templateNodeModule = new TemplateNodeModule();
        if (!startLightyModule(templateNodeModule)) {
            LOG.error("Unable to start TemplateNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        LOG.debug("CCSDK Plugins Lighty module was initialized successfully");
        return true;
    }

    protected boolean stopProcedure() {
        LOG.debug("Stopping CCSDK Plugins Lighty module...");

        boolean stopSuccessfull = true;

        if (!stopLightyModule(templateNodeModule)) {
            stopSuccessfull = false;
        }

        if (!stopLightyModule(sshApiCallModule)) {
            stopSuccessfull = false;
        }

        if (!stopLightyModule(restconfClientModule)) {
            stopSuccessfull = false;
        }

        if (!stopLightyModule(restApiCallNodeModule)) {
            stopSuccessfull = false;
        }

        if (!stopLightyModule(propertiesNodeModule)) {
            stopSuccessfull = false;
        }

        if (stopSuccessfull) {
            LOG.debug("CCSDK Plugins Lighty module was stopped successfully");
        } else {
            LOG.error("CCSDK Plugins Lighty module was not stopped successfully!");
        }
        return stopSuccessfull;
    }

    // TODO move this method somewhere to utils?
    private boolean startLightyModule(LightyModule lightyModule) {
        try {
            return lightyModule.start().get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Exception thrown while initializing {} in CCSDK Plugins Lighty module!", lightyModule.getClass(),
                    e);
            return false;
        }
    }

    // TODO move this method somewhere to utils?
    private boolean stopLightyModule(LightyModule lightyModule) {
        try {
            if (!lightyModule.shutdown().get()) {
                LOG.error("{} was not stopped successfully in CCSDK Plugins Lighty module!", lightyModule.getClass());
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            LOG.error("Exception thrown while shutting down {} in CCSDK Plugins Lighty module!", lightyModule.getClass(),
                    e);
            return false;
        }
    }

    public PropertiesNodeModule getPropertiesNodeModule() {
        return propertiesNodeModule;
    }

    public RestApiCallNodeModule getRestApiCallNodeModule() {
        return restApiCallNodeModule;
    }

    public RestconfClientModule getRestconfClientModule() {
        return restconfClientModule;
    }

    public SshApiCallModule getSshApiCallModule() {
        return sshApiCallModule;
    }

    public TemplateNodeModule getTemplateNodeModule() {
        return templateNodeModule;
    }
}
