/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights
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
import java.util.Properties;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ClusterHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.site.health.output.SitesBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.DatabaseHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.AdminHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.HaltAkkaTrafficOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ResumeAkkaTrafficOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteIdentifierOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverInputBuilder;


import org.onap.ccsdk.sli.core.sli.provider.MdsalHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrToolkitUtil extends MdsalHelper {
    private static final Logger LOG = LoggerFactory.getLogger(GrToolkitUtil.class);
    public static String PROPERTIES_FILE = "/opt/opendaylight/current/controller/configuration/gr-toolkit.properties";

    public static void loadProperties() {
        File file = new File(PROPERTIES_FILE);
        Properties properties = new Properties();
        InputStream input = null;
        if(file.isFile() && file.canRead()) {
            try {
                input = new FileInputStream(file);
                properties.load(input);
                LOG.info("Loaded properties from " + PROPERTIES_FILE);
                setProperties(properties);
            } catch (Exception e) {
                LOG.error("Failed to load properties " + PROPERTIES_FILE + "\n", e);
            } finally {
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        LOG.error("Failed to close properties file " + PROPERTIES_FILE + "\n", e);
                    }
                }
            }
        }
    }

    static {
        // Trick class loader into loading builders. Some of
        // these will be needed later by Reflection classes, but need
        // to explicitly "new" them here to get class loader to load them.

        ClusterHealthOutputBuilder b1 = new ClusterHealthOutputBuilder();
        SiteHealthOutputBuilder b2 = new SiteHealthOutputBuilder();
        SitesBuilder b3 = new SitesBuilder();
        DatabaseHealthOutputBuilder b4 = new DatabaseHealthOutputBuilder();
        AdminHealthOutputBuilder b5 = new AdminHealthOutputBuilder();
        HaltAkkaTrafficOutputBuilder b6 = new HaltAkkaTrafficOutputBuilder();
        ResumeAkkaTrafficOutputBuilder b7 = new ResumeAkkaTrafficOutputBuilder();
        SiteIdentifierOutputBuilder b8 = new SiteIdentifierOutputBuilder();
        FailoverOutputBuilder b9 = new FailoverOutputBuilder();
        FailoverInputBuilder b10 = new FailoverInputBuilder();
    }
}
