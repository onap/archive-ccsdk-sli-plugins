/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * * Copyright (C) 2017 AT&T Intellectual Property.
 * ================================================================================
 * Copyright (C) 2018 Samsung Electronics. All rights
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

package org.onap.ccsdk.sli.plugins.sshapicall.impl;

import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.plugins.sshapicall.SshApiCallNode;

import java.io.OutputStream;
import java.util.Map;

public class SshapiCallNodeImpl implements SshApiCallNode {
    @Override
    public void execCommand(Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException {
        //TODO: Implementation
    }

    @Override
    public void execCommandWithPty(Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException {
        //TODO: Implementation
    }
}
