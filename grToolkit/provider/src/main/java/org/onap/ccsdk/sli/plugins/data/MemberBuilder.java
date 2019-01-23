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

package org.onap.ccsdk.sli.plugins.data;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.cluster.health.output.MembersBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.CommitStatusBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.CommitStatus;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.ReplicasBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.Replicas;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.LeaderBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.member.Leader;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MemberBuilder extends MembersBuilder {
    public MemberBuilder(ClusterActor actor) {
        super();
        this.setAddress(actor.getNode());
        this.setRole(actor.getMember());
        this.setVoting(actor.isVoting());
        this.setUp(actor.isUp());
        this.setUnreachable(actor.isUnreachable());
        populateReplicas(actor.getReplicaShards());
        populateCommits(actor.getCommits());
        populateLeader(actor.getShardLeader());
        //actor.getNonReplicaShards();
    }

    private void populateLeader(ArrayList<String> shardLeader) {
        LeaderBuilder builder;
        this.setLeader((List) new ArrayList<Leader>());
        for(String leader : shardLeader) {
            builder = new LeaderBuilder();
            builder.setShard(leader);
            this.getLeader().add(builder.build());
        }
    }

    private void populateCommits(HashMap<String, Integer> commits) {
        CommitStatusBuilder builder;
        this.setCommitStatus((List) new ArrayList<CommitStatus>());
        for(String key : commits.keySet()) {
            if(commits.get(key) != 0) {
                builder = new CommitStatusBuilder();
                builder.setShard(key);
                builder.setDelta(commits.get(key));
                this.getCommitStatus().add(builder.build());
            }
        }
    }

    private void populateReplicas(ArrayList<String> replicaShards) {
        ReplicasBuilder builder;
        this.setReplicas((List) new ArrayList<Replicas>());
        for(String shard : replicaShards) {
            builder = new ReplicasBuilder();
            builder.setShard(shard);
            this.getReplicas().add(builder.build());
        }
    }
}
