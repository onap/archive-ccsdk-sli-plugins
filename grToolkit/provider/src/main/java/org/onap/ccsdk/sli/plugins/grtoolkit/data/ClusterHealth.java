/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights
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

package org.onap.ccsdk.sli.plugins.grtoolkit.data;

/**
 * A data container for Akka Cluster health.
 *
 * @author Anthony Haddox
 * @see org.onap.ccsdk.sli.plugins.grtoolkit.resolver.HealthResolver
 */
public class ClusterHealth {
    private Health health;

    public ClusterHealth() {
        health = Health.FAULTY;
    }

    public ClusterHealth withHealth(Health h) {
        this.health = h;
        return this;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }
}
