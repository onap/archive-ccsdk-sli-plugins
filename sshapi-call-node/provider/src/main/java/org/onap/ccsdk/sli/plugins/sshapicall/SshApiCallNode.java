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

import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import java.io.OutputStream;

public interface SshApiCallNode extends SvcLogicJavaPlugin {

    /**
     * Exec remote command over SSH. Return command execution status.
     * Command output is written to out or err stream.
     *
     * @param cmd command to execute
     * @param out content of sysout will go to this stream
     * @param err content of syserr will go to this stream
     * @return command execution status
     */
    void execCommand(String cmd, OutputStream out, OutputStream err);

    /**
     * Exec remote command over SSH with pseudo-tty. Return command execution status.
     * Command output is written to out stream only as pseudo-tty writes to one stream only.
     *
     * @param cmd command to execute
     * @param out content of sysout will go to this stream
     * @return command execution status
     */
    void execCommandWithPty(String cmd, OutputStream out);

}
