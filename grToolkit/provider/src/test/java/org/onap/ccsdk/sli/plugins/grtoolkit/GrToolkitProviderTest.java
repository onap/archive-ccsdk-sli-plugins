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

package org.onap.ccsdk.sli.plugins.grtoolkit;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.onap.ccsdk.sli.core.dblib.DBLibConnection;
import org.onap.ccsdk.sli.core.dblib.DbLibService;
import org.onap.ccsdk.sli.plugins.grtoolkit.data.ClusterActor;
import org.opendaylight.controller.cluster.access.concepts.MemberName;
import org.opendaylight.controller.cluster.datastore.DistributedDataStoreInterface;
import org.opendaylight.controller.cluster.datastore.utils.ActorContext;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.AdminHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ClusterHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.DatabaseHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteIdentifierOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class GrToolkitProviderTest {
    GrToolkitProvider provider;
    GrToolkitProvider providerSpy;
    DataBroker dataBroker;
    NotificationPublishService notificationProviderService;
    RpcProviderRegistry rpcProviderRegistry;
    DistributedDataStoreInterface configDatastore;
    DbLibService dbLibService;
    DBLibConnection connection;

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setup() {
        environmentVariables.set("SDNC_CONFIG_DIR","src/test/resources/");
        dataBroker = mock(DataBroker.class);
        notificationProviderService = mock(NotificationPublishService.class);
        rpcProviderRegistry = mock(RpcProviderRegistry.class);
        configDatastore = mock(DistributedDataStoreInterface.class);
        dbLibService = mock(DbLibService.class);
        connection = mock(DBLibConnection.class);

        ActorContext actorContext = mock(ActorContext.class);
        MemberName memberName = MemberName.forName("Test");

        when(actorContext.getCurrentMemberName()).thenReturn(memberName);
        when(configDatastore.getActorContext()).thenReturn(actorContext);

        try {
            when(connection.isReadOnly()).thenReturn(false);
            when(connection.isClosed()).thenReturn(false);
            when(dbLibService.isActive()).thenReturn(true);
            when(dbLibService.getConnection()).thenReturn(connection);
        } catch(SQLException e) {
            fail();
        }

        provider = new GrToolkitProvider(dataBroker, notificationProviderService,
                rpcProviderRegistry, configDatastore, dbLibService);
        providerSpy = spy(provider);
    }

    @Test
    public void closeTest() {
        try {
            provider.close();
        }
        catch(Exception e) {
            // Exception expected
        }
    }

    @Test
    public void onDataTreeChangedTest() {
        provider.onDataTreeChanged(new ArrayList());
        // onDataTreeChanged is an empty stub
    }

    @Test
    public void clusterHealthTest() {
        ListenableFuture<RpcResult<ClusterHealthOutput>> result = provider.clusterHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void siteHealthTest() {
        ListenableFuture<RpcResult<SiteHealthOutput>> result = provider.siteHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void siteHealth6NodeTest() {
        Map<String, ClusterActor> memberMap = new HashMap<>();
        ClusterActor actor;
        for(int ndx = 0; ndx < 6; ndx++) {
            actor = new ClusterActor();
            actor.setNode("member-" + (ndx + 1));
            actor.setUp(true);
            actor.setUnreachable(false);

            memberMap.put(actor.getNode(),  actor);
        }

        try {
            Field field = provider.getClass().getDeclaredField("siteConfiguration");
            field.setAccessible(true);
            field.set(provider, GrToolkitProvider.SiteConfiguration.GEO);

            field = provider.getClass().getDeclaredField("memberMap");
            field.setAccessible(true);
            field.set(provider, memberMap);


            actor = new ClusterActor();
            actor.setNode("member-1");
            field = provider.getClass().getDeclaredField("self");
            field.setAccessible(true);
            field.set(provider, actor);

            field = provider.getClass().getDeclaredField("member");
            field.setAccessible(true);
            field.set(provider, actor.getNode());
        }
        catch(IllegalAccessException | NoSuchFieldException e) {
            fail();
        }

        ListenableFuture<RpcResult<SiteHealthOutput>> result = provider.siteHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void databaseHealthTest() {
        ListenableFuture<RpcResult<DatabaseHealthOutput>> result = provider.databaseHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void databaseHealthWhenROTest() {
        try {
            when(connection.isReadOnly()).thenReturn(true);
        } catch(SQLException e) {
            fail();
        }
        ListenableFuture<RpcResult<DatabaseHealthOutput>> result = provider.databaseHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void databaseHealthWhenExceptionTest() {
        try {
            when(connection.isReadOnly()).thenThrow(new SQLException());
        } catch(SQLException e) {
            //expected
        }
        ListenableFuture<RpcResult<DatabaseHealthOutput>> result = provider.databaseHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void adminHealthTest() {
        ListenableFuture<RpcResult<AdminHealthOutput>> result = provider.adminHealth(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void siteIdentifierTest() {
        ListenableFuture<RpcResult<SiteIdentifierOutput>> result = provider.siteIdentifier(null);
        try {
            assertEquals("200", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void failoverTest() {
        ListenableFuture<RpcResult<FailoverOutput>> result = provider.failover(null);
        try {
            assertEquals("400", result.get().getResult().getStatus());
        } catch(InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void executeCommandTest() {
        try {
            Method method = provider.getClass().getDeclaredMethod("executeCommand", String.class);
            method.setAccessible(true);
            method.invoke(provider, "ls");
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void isolateSiteFromClusterTest() {
        try {
            ClusterActor actor = new ClusterActor();
            actor.setNode("some-node");
            actor.setAkkaPort("2550");
            ArrayList<ClusterActor> activeList = new ArrayList<>();
            activeList.add(actor);
            ArrayList<ClusterActor> standbyList = new ArrayList<>();
            standbyList.add(actor);
            Method method = provider.getClass().getDeclaredMethod("isolateSiteFromCluster", ArrayList.class, ArrayList.class, String.class);
            method.setAccessible(true);
            method.invoke(provider, activeList, standbyList, "80");
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void downUnreachableNodesTest() {
        try {
            ClusterActor actor = new ClusterActor();
            actor.setNode("some-node");
            actor.setAkkaPort("2550");
            ArrayList<ClusterActor> activeList = new ArrayList<>();
            activeList.add(actor);
            ArrayList<ClusterActor> standbyList = new ArrayList<>();
            standbyList.add(actor);
            Method method = provider.getClass().getDeclaredMethod("downUnreachableNodes", ArrayList.class, ArrayList.class, String.class);
            method.setAccessible(true);
            method.invoke(provider, activeList, standbyList, "80");
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void changeClusterVotingTest() {
        try {
            ClusterActor actor = new ClusterActor();
            actor.setMember("some-member");
            actor.setNode("some-Node");
            ArrayList<ClusterActor> activeList = new ArrayList<>();
            activeList.add(actor);
            ArrayList<ClusterActor> standbyList = new ArrayList<>();
            standbyList.add(actor);
            Field field = provider.getClass().getDeclaredField("self");
            field.setAccessible(true);
            field.set(provider, actor);
            Method method = provider.getClass().getDeclaredMethod("changeClusterVoting", FailoverOutputBuilder.class, ArrayList.class, ArrayList.class, String.class);
            method.setAccessible(true);
            method.invoke(provider, new FailoverOutputBuilder(), activeList, standbyList, "80");
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail();
        }
    }

    @Test
    public void backupMdSalTest() {
        try {
            ClusterActor actor = new ClusterActor();
            actor.setNode("some-Node");
            actor.setAkkaPort("2550");
            ArrayList<ClusterActor> activeList = new ArrayList<>();
            activeList.add(actor);
            Method method = provider.getClass().getDeclaredMethod("backupMdSal", ArrayList.class, String.class);
            method.setAccessible(true);
            method.invoke(provider, activeList, "80");
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

}
