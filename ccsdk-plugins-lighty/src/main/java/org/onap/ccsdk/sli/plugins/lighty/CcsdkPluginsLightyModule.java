/*
 * ============LICENSE_START==========================================
 * Copyright (c) 2019 PANTHEON.tech s.r.o.
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END============================================
 *
 */
package org.onap.ccsdk.sli.plugins.lighty;

import io.lighty.core.controller.api.AbstractLightyModule;
import org.onap.ccsdk.sli.core.lighty.common.CcsdkLightyUtils;
import org.onap.ccsdk.sli.plugins.prop.lighty.PropertiesNodeModule;
import org.onap.ccsdk.sli.plugins.prop.lighty.RestApiCallNodeModule;
import org.onap.ccsdk.sli.plugins.restconf.lighty.RestconfClientModule;
import org.onap.ccsdk.sli.plugins.sshapicall.lighty.SshApiCallModule;
import org.onap.ccsdk.sli.plugins.template.lighty.TemplateNodeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the {@link io.lighty.core.controller.api.LightyModule} that groups all other LightyModules 
 * from the ccsdk-sli-plugins repository so they can be all treated as one component (for example started/stopped at once).
 * For more information about the lighty.io visit the website https://lighty.io.
 */
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
        if (!CcsdkLightyUtils.startLightyModule(propertiesNodeModule)) {
            LOG.error("Unable to start PropertiesNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.restApiCallNodeModule = new RestApiCallNodeModule();
        if (!CcsdkLightyUtils.startLightyModule(restApiCallNodeModule)) {
            LOG.error("Unable to start RestApiCallNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.restconfClientModule = new RestconfClientModule(restApiCallNodeModule.getPropertiesNode());
        if (!CcsdkLightyUtils.startLightyModule(restconfClientModule)) {
            LOG.error("Unable to start RestconfClientModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.sshApiCallModule = new SshApiCallModule();
        if (!CcsdkLightyUtils.startLightyModule(sshApiCallModule)) {
            LOG.error("Unable to start SshApiCallModule in CCSDK Plugins Lighty module!");
            return false;
        }

        this.templateNodeModule = new TemplateNodeModule();
        if (!CcsdkLightyUtils.startLightyModule(templateNodeModule)) {
            LOG.error("Unable to start TemplateNodeModule in CCSDK Plugins Lighty module!");
            return false;
        }

        LOG.debug("CCSDK Plugins Lighty module was initialized successfully");
        return true;
    }

    protected boolean stopProcedure() {
        LOG.debug("Stopping CCSDK Plugins Lighty module...");

        boolean stopSuccessful = true;

        if (!CcsdkLightyUtils.stopLightyModule(templateNodeModule)) {
            stopSuccessful = false;
        }

        if (!CcsdkLightyUtils.stopLightyModule(sshApiCallModule)) {
            stopSuccessful = false;
        }

        if (!CcsdkLightyUtils.stopLightyModule(restconfClientModule)) {
            stopSuccessful = false;
        }

        if (!CcsdkLightyUtils.stopLightyModule(restApiCallNodeModule)) {
            stopSuccessful = false;
        }

        if (!CcsdkLightyUtils.stopLightyModule(propertiesNodeModule)) {
            stopSuccessful = false;
        }

        if (stopSuccessful) {
            LOG.debug("CCSDK Plugins Lighty module was stopped successfully");
        } else {
            LOG.error("CCSDK Plugins Lighty module was not stopped successfully!");
        }
        return stopSuccessful;
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
