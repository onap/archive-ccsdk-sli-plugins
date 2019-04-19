package org.onap.ccsdk.sli.plugins.grtoolkit.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MemberBuilderTest {
    MemberBuilder builder;
    ClusterActor actor;

    @Before
    public void setUp() {
        actor = new ClusterActor();
        actor.setUp(true);
        actor.setVoting(true);
    }

    @Test
    public void constructorTest() {
        ArrayList<String> actorList = new ArrayList<>();
        ArrayList<String> shardList = new ArrayList<>();
        HashMap<String, Integer> commitMap = new HashMap<>();
        actorList.add("Some-Actor");
        shardList.add("Some-shard");
        commitMap.put("Some-shard", 4);
        commitMap.put("Some-other-shard", -4);
        actor.setShardLeader(actorList);
        actor.setReplicaShards(shardList);
        actor.setNonReplicaShards(shardList);
        actor.setCommits(commitMap);
        assertNotNull(actor.toString());
        assertEquals("", actor.getSite());
        assertEquals(1, actor.getNonReplicaShards().size());
        builder = new MemberBuilder(actor);
        assertNotNull(builder.build());
    }
}