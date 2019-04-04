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
package org.onap.ccsdk.sli.plugins.restconf.lighty;

import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyModule;
import org.onap.ccsdk.sli.plugins.restapicall.RestapiCallNode;
import org.onap.ccsdk.sli.plugins.restconfapicall.RestconfApiCallNode;
import org.onap.ccsdk.sli.plugins.restconfdiscovery.RestconfDiscoveryNode;

/**
 * The implementation of the {@link io.lighty.core.controller.api.LightyModule} that manages and provides services from
 * the restconf-client-provider artifact.
 */
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
