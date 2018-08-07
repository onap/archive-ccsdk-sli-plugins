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

package org.onap.ccsdk.sli.plugins.template;

import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateNode implements SvcLogicJavaPlugin {
    private static final Logger log = LoggerFactory.getLogger(TemplateNode.class);
    public final static String TEMPLATE_PATH = "templatePath";
    public final static String OUTPUT_PATH_KEY = "output";
    public final static String PREFIX_KEY = "prefix";
    public final static String REQUIRED_PARAMETERS_ERROR_MESSAGE = "templateName & outputPath are required fields";
    protected VelocityEngine ve;
    
    public TemplateNode() {
        ve = new VelocityEngine();
        setProperties();
        ve.init();
    }
    
    protected void setProperties() {
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "/read/from/configuration/file");
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "false");
    }

    public void evaluateTemplate(Map<String, String> params, SvcLogicContext ctx) throws SvcLogicException {
        String templateName = params.get(TEMPLATE_PATH);
        String outputPath = params.get(OUTPUT_PATH_KEY);
        String prefix = params.get(PREFIX_KEY);

        if (prefix != null && prefix.length() > 0) {
            outputPath = prefix + "." + outputPath;
        }

        if (templateName == null || outputPath == null) {
            throw new SvcLogicException(REQUIRED_PARAMETERS_ERROR_MESSAGE);
        } else {
            try {
                Template template = ve.getTemplate(templateName);
                VelocityContext context = new VelocityContext();
                context.put("ctx", ctx);
                context.put("params", params);
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                ctx.setAttribute(outputPath, sw.toString());
            } catch (Exception e) {
                throw new SvcLogicException(e.getMessage());
            }
        }
    }

}
