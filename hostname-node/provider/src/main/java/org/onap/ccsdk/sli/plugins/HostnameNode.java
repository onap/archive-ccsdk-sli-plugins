/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
 * 			reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.sli.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostnameNode implements SvcLogicJavaPlugin {

    private static final Logger log = LoggerFactory.getLogger(HostnameNode.class);
    private static final String VARNAME= "var-name";

    public void getHostname(Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException {

        String varname = null;

        // Parameter "hostname-var" is name of variable to set
        if (paramMap.containsKey(VARNAME)) {
            varname = paramMap.get(VARNAME);
        }

        if (varname != null) {
            String hostname = null;
            try {
                hostname = InetAddress.getLocalHost().getHostName();
                ctx.setAttribute(varname, hostname);
            } catch (UnknownHostException e) {
                throw new SvcLogicException("Cannot get hostname");
            }
        }

    }

}
