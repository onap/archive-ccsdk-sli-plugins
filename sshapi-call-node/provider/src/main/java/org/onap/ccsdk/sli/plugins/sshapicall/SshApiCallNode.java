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

package org.onap.ccsdk.sli.plugins.sshapicall;

import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import java.io.OutputStream;
import java.util.Map;

public interface SshApiCallNode extends SvcLogicJavaPlugin {

    /**
     * Allows Directed Graphs  the ability to interact with SSH APIs.
     * @param paramMap HashMap<String,String> of parameters passed by the DG to this function
     * <table border="1">
     *  <thead><th>parameter</th><th>Mandatory/Optional</th><th>description</th><th>example values</th></thead>
     *  <tbody>
     *      <tr><td>templateFileName</td><td>Optional</td><td>full path to template file that can be used to build a request</td><td>/sdncopt/bvc/sshapi/templates/vnf_service-configuration-operation_minimal.json</td></tr>
     *      <tr><td>sshapiUrl</td><td>Mandatory</td><td>url to make the SSH connection request to.</td></tr>
     *      <tr><td>sshapiUser</td><td>Optional</td><td>user name to use for ssh basic authentication</td><td>sdnc_ws</td></tr>
     *      <tr><td>sshapiPassword</td><td>Optional</td><td>unencrypted password to use for ssh basic authentication</td><td>plain_password</td></tr>
     *      <tr><td>sshKey</td><td>Optional</td><td>Consumer SSH key to use for ssh authentication</td><td>plain_key</td></tr>
     *      <tr><td>cmd</td><td>Mandatory</td><td>ssh command to be executed on the server.</td><td>get post put delete patch</td></tr>
     *      <tr><td>responsePrefix</td><td>Optional</td><td>location the response will be written to in context memory</td><td>tmp.sshapi.result</td></tr>
     *      <tr><td>listName[i]</td><td>Optional</td><td>Used for processing XML responses with repeating elements.</td>vpn-information.vrf-details<td></td></tr>
     *      <tr><td>convertResponse </td><td>Optional</td><td>whether the response should be converted</td><td>true or false</td></tr>
     *      <tr><td>dumpHeaders</td><td>Optional</td><td>when true writes ssh response content to context memory</td><td>true or false</td></tr>
     *      <tr><td>returnRequestPayload</td><td>Optional</td><td>used to return payload built in the request</td><td>true or false</td></tr>
     *  </tbody>
     * </table>
     * Exec remote command over SSH. Return command execution status.
     * Command output is written to out or err stream.
     *
     * @param ctx Reference to context memory
     */
    void execCommand(Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException;

    /**
     * Allows Directed Graphs  the ability to interact with SSH APIs.
     * @param paramMap HashMap<String,String> of parameters passed by the DG to this function
     * <table border="1">
     *  <thead><th>parameter</th><th>Mandatory/Optional</th><th>description</th><th>example values</th></thead>
     *  <tbody>
     *      <tr><td>templateFileName</td><td>Optional</td><td>full path to template file that can be used to build a request</td><td>/sdncopt/bvc/sshapi/templates/vnf_service-configuration-operation_minimal.json</td></tr>
     *      <tr><td>sshapiUrl</td><td>Mandatory</td><td>url to make the SSH connection request to.</td></tr>
     *      <tr><td>sshapiUser</td><td>Optional</td><td>user name to use for ssh basic authentication</td><td>sdnc_ws</td></tr>
     *      <tr><td>sshapiPassword</td><td>Optional</td><td>unencrypted password to use for ssh basic authentication</td><td>plain_password</td></tr>
     *      <tr><td>sshKey</td><td>Optional</td><td>Consumer SSH key to use for ssh authentication</td><td>plain_key</td></tr>
     *      <tr><td>cmd</td><td>Mandatory</td><td>ssh command to be executed on the server.</td><td>get post put delete patch</td></tr>
     *      <tr><td>responsePrefix</td><td>Optional</td><td>location the response will be written to in context memory</td><td>tmp.sshapi.result</td></tr>
     *      <tr><td>listName[i]</td><td>Optional</td><td>Used for processing XML responses with repeating elements.</td>vpn-information.vrf-details<td></td></tr>
     *      <tr><td>convertResponse </td><td>Optional</td><td>whether the response should be converted</td><td>true or false</td></tr>
     *      <tr><td>dumpHeaders</td><td>Optional</td><td>when true writes ssh response content to context memory</td><td>true or false</td></tr>
     *      <tr><td>returnRequestPayload</td><td>Optional</td><td>used to return payload built in the request</td><td>true or false</td></tr>
     *  </tbody>
     * </table>
     * Exec remote command over SSH with pseudo-tty. Return command execution status.
     * Command output is written to out stream only as pseudo-tty writes to one stream only.
     *
     *  @param ctx Reference to context memory
     */
    void execCommandWithPty(Map<String, String> paramMap, SvcLogicContext ctx)  throws SvcLogicException ;

}
