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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.onap.ccsdk.sli.core.dblib.DbLibService;
import org.onap.ccsdk.sli.plugins.data.ClusterActor;
import org.onap.ccsdk.sli.plugins.data.MemberBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.opendaylight.controller.cluster.datastore.DistributedDataStoreInterface;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.AdminHealthInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.AdminHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.AdminHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ClusterHealthInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ClusterHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ClusterHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.DatabaseHealthInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.DatabaseHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.DatabaseHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.FailoverOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.GrToolkitService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.HaltAkkaTrafficInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.HaltAkkaTrafficOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.HaltAkkaTrafficOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.Member;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ResumeAkkaTrafficInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ResumeAkkaTrafficOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.ResumeAkkaTrafficOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.Site;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteHealthInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteHealthOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteHealthOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteIdentifierInput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteIdentifierOutput;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.SiteIdentifierOutputBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.site.health.output.SitesBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrToolkitProvider implements AutoCloseable, GrToolkitService, DataTreeChangeListener {
    private static final String APP_NAME = "gr-toolkit";
    private static final String PROPERTIES_FILE = System.getenv("SDNC_CONFIG_DIR") + "/gr-toolkit.properties";
    private static final String HEALTHY = "HEALTHY";
    private static final String FAULTY = "FAULTY";
    private static final String VALUE = "value";
    private String akkaConfig;
    private String jolokiaClusterPath;
    private String shardManagerPath;
    private String shardPathTemplate;
    private String credentials;
    private String httpProtocol;
    private String siteIdentifier = System.getenv("SITE_NAME");
    private final Logger log = LoggerFactory.getLogger(GrToolkitProvider.class);
    private final ExecutorService executor;
    protected DataBroker dataBroker;
    protected NotificationPublishService notificationService;
    protected RpcProviderRegistry rpcRegistry;
    protected BindingAwareBroker.RpcRegistration<GrToolkitService> rpcRegistration;
    protected DbLibService dbLib;
    private String member;
    private ClusterActor self;
    private HashMap<String, ClusterActor> memberMap;
    private SiteConfiguration siteConfiguration;
    private Properties properties;
    private DistributedDataStoreInterface configDatastore;
    public GrToolkitProvider(DataBroker dataBroker,
                             NotificationPublishService notificationProviderService,
                             RpcProviderRegistry rpcProviderRegistry,
                             DistributedDataStoreInterface configDatastore,
                             DbLibService dbLibService) {
        this.log.info("Creating provider for {}", APP_NAME);
        this.executor = Executors.newFixedThreadPool(1);
        this.dataBroker = dataBroker;
        this.notificationService = notificationProviderService;
        this.rpcRegistry = rpcProviderRegistry;
        this.configDatastore = configDatastore;
        this.dbLib = dbLibService;
        initialize();
    }

    private void initialize() {
        log.info("Initializing provider for {}", APP_NAME);
        // Create the top level containers
        createContainers();
        setProperties();
        defineMembers();

        rpcRegistration = rpcRegistry.addRpcImplementation(GrToolkitService.class, this);
        log.info("Initialization complete for {}", APP_NAME);
    }

    private void setProperties() {
        log.info("Loading properties from {}", PROPERTIES_FILE);
        properties = new Properties();
        File propertiesFile = new File(PROPERTIES_FILE);
        if(!propertiesFile.exists()) {
            log.warn("Properties file not found.");
            return;
        }
        try(FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
            properties.load(fileInputStream);
            if(!properties.containsKey(PropertyKeys.SITE_IDENTIFIER)) {
                properties.put(PropertyKeys.SITE_IDENTIFIER, "Unknown Site");
            }
            String port = "true".equals(properties.getProperty(PropertyKeys.CONTROLLER_USE_SSL).trim()) ? properties.getProperty(PropertyKeys.CONTROLLER_PORT_SSL).trim() : properties.getProperty(PropertyKeys.CONTROLLER_PORT_HTTP).trim();
            httpProtocol = "true".equals(properties.getProperty(PropertyKeys.CONTROLLER_USE_SSL).trim()) ? "https://" : "http://";
            akkaConfig = properties.getProperty(PropertyKeys.AKKA_CONF_LOCATION).trim();
            jolokiaClusterPath = ":" + port + properties.getProperty(PropertyKeys.MBEAN_CLUSTER).trim();
            shardManagerPath = ":" + port + properties.getProperty(PropertyKeys.MBEAN_SHARD_MANAGER).trim();
            shardPathTemplate = ":" + port + properties.getProperty(PropertyKeys.MBEAN_SHARD_CONFIG).trim();
            if(siteIdentifier == null || siteIdentifier.isEmpty()) {
                siteIdentifier = properties.getProperty(PropertyKeys.SITE_IDENTIFIER).trim();
            }
            credentials = properties.getProperty(PropertyKeys.CONTROLLER_CREDENTIALS).trim();
            log.info("Loaded properties.");
        } catch(IOException e) {
            log.error("Error loading properties.", e);
        }
    }

    private void defineMembers() {
        member = configDatastore.getActorContext().getCurrentMemberName().getName();
        log.info("Cluster member: {}", member);

        log.info("Parsing akka.conf for cluster memberMap...");
        try {
            File akkaConfigFile = new File(this.akkaConfig);
            try(FileReader fileReader = new FileReader(akkaConfigFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    if(line.contains("seed-nodes =")) {
                        parseSeedNodes(line);
                        break;
                    }
                }
            }
        } catch(IOException e) {
            log.error("Couldn't load akka", e);
        }
        log.info("self:\n{}", self);
    }

    private void createContainers() {
        // Replace with MD-SAL write for FailoverStatus
    }

    protected void initializeChild() {
        // Override if you have custom initialization intelligence
    }

    @Override
    public void close() throws Exception {
        log.info("Closing provider for {}", APP_NAME);
        executor.shutdown();
        rpcRegistration.close();
        log.info("Successfully closed provider for {}", APP_NAME);
    }

    @Override
    public void onDataTreeChanged(@Nonnull Collection changes) {
        log.info("onDataTreeChanged() called. but there is no change here");
    }

    @Override
    public ListenableFuture<RpcResult<ClusterHealthOutput>> clusterHealth(ClusterHealthInput input) {
        log.info("{}:cluster-health invoked.", APP_NAME);
        getControllerHealth();
        return buildClusterHealthOutput("200");
    }

    @Override
    public ListenableFuture<RpcResult<SiteHealthOutput>> siteHealth(SiteHealthInput input) {
        log.info("{}:site-health invoked.", APP_NAME);
        getControllerHealth();
        return buildSiteHealthOutput("200", getAdminHealth(), getDatabaseHealth());
    }

    @Override
    public ListenableFuture<RpcResult<DatabaseHealthOutput>> databaseHealth(DatabaseHealthInput input) {
        log.info("{}:database-health invoked.", APP_NAME);
        DatabaseHealthOutputBuilder outputBuilder = new DatabaseHealthOutputBuilder();
        outputBuilder.setStatus("200");
        outputBuilder.setHealth(getDatabaseHealth());

        return Futures.immediateFuture(RpcResultBuilder.<DatabaseHealthOutput>status(true).withResult(outputBuilder.build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<AdminHealthOutput>> adminHealth(AdminHealthInput input) {
        log.info("{}:admin-health invoked.", APP_NAME);
        AdminHealthOutputBuilder outputBuilder = new AdminHealthOutputBuilder();
        outputBuilder.setStatus("200");
        outputBuilder.setHealth(getAdminHealth());

        return Futures.immediateFuture(RpcResultBuilder.<AdminHealthOutput>status(true).withResult(outputBuilder.build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<HaltAkkaTrafficOutput>> haltAkkaTraffic(HaltAkkaTrafficInput input) {
        log.info("{}:halt-akka-traffic invoked.", APP_NAME);
        HaltAkkaTrafficOutputBuilder outputBuilder = new HaltAkkaTrafficOutputBuilder();
        outputBuilder.setStatus("200");
        modifyIpTables(IpTables.ADD, input.getNodeInfo().toArray());

        return Futures.immediateFuture(RpcResultBuilder.<HaltAkkaTrafficOutput>status(true).withResult(outputBuilder.build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<ResumeAkkaTrafficOutput>> resumeAkkaTraffic(ResumeAkkaTrafficInput input) {
        log.info("{}:resume-akka-traffic invoked.", APP_NAME);
        ResumeAkkaTrafficOutputBuilder outputBuilder = new ResumeAkkaTrafficOutputBuilder();
        outputBuilder.setStatus("200");
        modifyIpTables(IpTables.DELETE, input.getNodeInfo().toArray());

        return Futures.immediateFuture(RpcResultBuilder.<ResumeAkkaTrafficOutput>status(true).withResult(outputBuilder.build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<SiteIdentifierOutput>> siteIdentifier(SiteIdentifierInput input) {
        log.info("{}:site-identifier invoked.", APP_NAME);
        SiteIdentifierOutputBuilder outputBuilder = new SiteIdentifierOutputBuilder();
        outputBuilder.setStatus("200");
        outputBuilder.setId(siteIdentifier);

        return Futures.immediateFuture(RpcResultBuilder.<SiteIdentifierOutput>status(true).withResult(outputBuilder.build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<FailoverOutput>> failover(FailoverInput input) {
        log.info("{}:failover invoked.", APP_NAME);
        FailoverOutputBuilder outputBuilder = new FailoverOutputBuilder();
        if(siteConfiguration != SiteConfiguration.GEO) {
            log.info("Cannot failover non-GEO site.");
            outputBuilder.setMessage("Failover aborted. This is not a GEO configuration.");
            outputBuilder.setStatus("400");
            return Futures.immediateFuture(RpcResultBuilder.<FailoverOutput>status(true).withResult(outputBuilder.build()).build());
        }
        ArrayList<ClusterActor> activeSite = new ArrayList<>();
        ArrayList<ClusterActor> standbySite = new ArrayList<>();

        log.info("Performing preliminary cluster health check...");
        // Necessary to populate all member info. Health is not used for judgement calls.
        getControllerHealth();

        log.info("Determining active site...");
        for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
            String key = entry.getKey();
            ClusterActor clusterActor = entry.getValue();
            if(clusterActor.isVoting()) {
                activeSite.add(clusterActor);
                log.debug("Active Site member: {}", key);
            }
            else {
                standbySite.add(clusterActor);
                log.debug("Standby Site member: {}", key);
            }
        }

        String port = "true".equals(properties.getProperty(PropertyKeys.CONTROLLER_USE_SSL)) ? properties.getProperty(PropertyKeys.CONTROLLER_PORT_SSL) : properties.getProperty(PropertyKeys.CONTROLLER_PORT_HTTP);

        if(Boolean.parseBoolean(input.getBackupData())) {
            backupMdSal(activeSite, port);
        }

        if(!changeClusterVoting(outputBuilder, activeSite, standbySite, port))
            return Futures.immediateFuture(RpcResultBuilder.<FailoverOutput>status(true).withResult(outputBuilder.build()).build());

        if(Boolean.parseBoolean(input.getIsolate())) {
            isolateSiteFromCluster(activeSite, standbySite, port);

            if(Boolean.parseBoolean(input.getDownUnreachable())) {
                downUnreachableNodes(activeSite, standbySite, port);
            }
        }

        log.info("{}:failover complete.", APP_NAME);

        outputBuilder.setMessage("Failover complete.");
        outputBuilder.setStatus("200");
        return Futures.immediateFuture(RpcResultBuilder.<FailoverOutput>status(true).withResult(outputBuilder.build()).build());
    }

    private void isolateSiteFromCluster(ArrayList<ClusterActor> activeSite, ArrayList<ClusterActor> standbySite, String port) {
        log.info("Halting Akka traffic...");
        for(ClusterActor actor : standbySite) {
            try {
                log.info("Halting Akka traffic for: {}", actor.getNode());
                // Build JSON with activeSite actor Node and actor  AkkaPort
                JSONObject akkaInput = new JSONObject();
                JSONObject inputBlock = new JSONObject();
                JSONArray votingStateArray = new JSONArray();
                JSONObject nodeInfo;
                for(ClusterActor node : activeSite) {
                    nodeInfo = new JSONObject();
                    nodeInfo.put("node", node.getNode());
                    nodeInfo.put("port", node.getAkkaPort());
                    votingStateArray.put(nodeInfo);
                }
                inputBlock.put("node-info", votingStateArray);
                akkaInput.put("input", inputBlock);
                getRequestContent(httpProtocol + actor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:halt-akka-traffic", HttpMethod.POST, akkaInput.toString());
            } catch(IOException e) {
                log.error("Could not halt Akka traffic for: " + actor.getNode(), e);
            }
        }
    }

    private void downUnreachableNodes(ArrayList<ClusterActor> activeSite, ArrayList<ClusterActor> standbySite, String port) {
        log.info("Setting site unreachable...");
        JSONObject jolokiaInput = new JSONObject();
        jolokiaInput.put("type", "EXEC");
        jolokiaInput.put("mbean", "akka:type=Cluster");
        jolokiaInput.put("operation", "down");
        JSONArray arguments = new JSONArray();
        for(ClusterActor actor : activeSite) {
            // Build Jolokia input
            // May need to change from akka port to actor.getAkkaPort()
            arguments.put("akka.tcp://opendaylight-cluster-data@" + actor.getNode() + ":" + properties.getProperty(PropertyKeys.CONTROLLER_PORT_AKKA));
        }
        jolokiaInput.put("arguments", arguments);
        log.debug("{}", jolokiaInput);
        try {
            log.info("Setting nodes unreachable");
            getRequestContent(httpProtocol + standbySite.get(0).getNode() + ":" + port + "/jolokia", HttpMethod.POST, jolokiaInput.toString());
        } catch(IOException e) {
            log.error("Error setting nodes unreachable", e);
        }
    }

    private boolean changeClusterVoting(FailoverOutputBuilder outputBuilder, ArrayList<ClusterActor> activeSite, ArrayList<ClusterActor> standbySite, String port) {
        log.info("Changing voting for all shards to standby site...");
        try {
            JSONObject votingInput = new JSONObject();
            JSONObject inputBlock = new JSONObject();
            JSONArray votingStateArray = new JSONArray();
            JSONObject memberVotingState;
            for(ClusterActor actor : activeSite) {
                memberVotingState = new JSONObject();
                memberVotingState.put("member-name", actor.getMember());
                memberVotingState.put("voting", false);
                votingStateArray.put(memberVotingState);
            }
            for(ClusterActor actor : standbySite) {
                memberVotingState = new JSONObject();
                memberVotingState.put("member-name", actor.getMember());
                memberVotingState.put("voting", true);
                votingStateArray.put(memberVotingState);
            }
            inputBlock.put("member-voting-state", votingStateArray);
            votingInput.put("input", inputBlock);
            log.debug("{}", votingInput);
            // Change voting all shards
            getRequestContent(httpProtocol + self.getNode() + ":" + port + "/restconf/operations/cluster-admin:change-member-voting-states-for-all-shards", HttpMethod.POST, votingInput.toString());
        } catch(IOException e) {
            log.error("Changing voting", e);
            outputBuilder.setMessage("Failover aborted. Failed to change voting.");
            outputBuilder.setStatus("500");
            return false;
        }
        return true;
    }

    private void backupMdSal(ArrayList<ClusterActor> activeSite, String port) {
        log.info("Backing up data...");
        try {
            log.info("Scheduling backup for: {}", activeSite.get(0).getNode());
            getRequestContent(httpProtocol + activeSite.get(0).getNode() + ":" + port + "/restconf/operations/data-export-import:schedule-export", HttpMethod.POST, "{ \"input\": { \"run-at\": \"30\" } }");
        } catch(IOException e) {
            log.error("Error backing up MD-SAL", e);
        }
        for(ClusterActor actor : activeSite) {
            try {
                // Move data offsite
                log.info("Backing up data for: {}", actor.getNode());
                getRequestContent(httpProtocol + actor.getNode() + ":" + port + "/restconf/operations/daexim-offsite-backup:backup-data", HttpMethod.POST);
            } catch(IOException e) {
                log.error("Error backing up data.", e);
            }
        }
    }

    private ListenableFuture<RpcResult<ClusterHealthOutput>> buildClusterHealthOutput(String statusCode) {
        ClusterHealthOutputBuilder outputBuilder = new ClusterHealthOutputBuilder();
        outputBuilder.setStatus(statusCode);
        outputBuilder.setMembers((List) new ArrayList<Member>());
        int site1Health = 0;
        int site2Health = 0;

        for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
            ClusterActor clusterActor = entry.getValue();
            if(clusterActor.isUp() && !clusterActor.isUnreachable()) {
                if(ClusterActor.SITE_1.equals(clusterActor.getSite()))
                    site1Health++;
                else if(ClusterActor.SITE_2.equals(clusterActor.getSite()))
                    site2Health++;
            }
            outputBuilder.getMembers().add(new MemberBuilder(clusterActor).build());
        }
        if(siteConfiguration == SiteConfiguration.SOLO) {
            outputBuilder.setSite1Health(HEALTHY);
        }
        else if(site1Health > 1) {
            outputBuilder.setSite1Health(HEALTHY);
        }
        else {
            outputBuilder.setSite1Health(FAULTY);
        }

        if(siteConfiguration == SiteConfiguration.GEO && site2Health > 1) {
            outputBuilder.setSite2Health(HEALTHY);
        }
        else if(siteConfiguration == SiteConfiguration.GEO) {
            outputBuilder.setSite2Health(FAULTY);
        }

        RpcResult<ClusterHealthOutput> rpcResult = RpcResultBuilder.<ClusterHealthOutput>status(true).withResult(outputBuilder.build()).build();
        return Futures.immediateFuture(rpcResult);
    }

    private ListenableFuture<RpcResult<SiteHealthOutput>> buildSiteHealthOutput(String statusCode, String adminHealth, String databaseHealth) {
        SiteHealthOutputBuilder outputBuilder = new SiteHealthOutputBuilder();
        outputBuilder.setStatus(statusCode);
        outputBuilder.setSites((List) new ArrayList<Site>());

        if(siteConfiguration != SiteConfiguration.GEO) {
            int healthyODLs = 0;
            SitesBuilder builder = new SitesBuilder();
            for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
                ClusterActor clusterActor = entry.getValue();
                if(clusterActor.isUp() && !clusterActor.isUnreachable()) {
                    healthyODLs++;
                }
            }
            if(siteConfiguration != SiteConfiguration.SOLO) {
                builder.setHealth(HEALTHY);
                builder.setRole("ACTIVE");
                builder.setId(siteIdentifier);
            }
            else {
                builder = getSitesBuilder(healthyODLs, true, HEALTHY.equals(adminHealth), HEALTHY.equals(databaseHealth), siteIdentifier);
            }
            outputBuilder.getSites().add(builder.build());
        }
        else {
            int site1HealthyODLs = 0;
            int site2HealthyODLs = 0;
            boolean site1Voting = false;
            boolean site2Voting = false;
            boolean performedCrossSiteHealthCheck = false;
            boolean crossSiteAdminHealthy = false;
            boolean crossSiteDbHealthy = false;
            String crossSiteIdentifier = "UNKNOWN_SITE";
            String port = "true".equals(properties.getProperty(PropertyKeys.CONTROLLER_USE_SSL)) ? properties.getProperty(PropertyKeys.CONTROLLER_PORT_SSL) : properties.getProperty(PropertyKeys.CONTROLLER_PORT_HTTP);
            if(isSite1()) {
                // Make calls over to site 2 healthchecks
                for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
                    ClusterActor clusterActor = entry.getValue();
                    if(clusterActor.isUp() && !clusterActor.isUnreachable()) {
                        if(ClusterActor.SITE_1.equals(clusterActor.getSite())) {
                            site1HealthyODLs++;
                            if(clusterActor.isVoting()) {
                                site1Voting = true;
                            }
                        }
                        else {
                            site2HealthyODLs++;
                            if(clusterActor.isVoting()) {
                                site2Voting = true;
                            }
                            if(!performedCrossSiteHealthCheck) {
                                try {
                                    String content = getRequestContent(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:site-identifier", HttpMethod.POST);
                                    crossSiteIdentifier = new JSONObject(content).getJSONObject("output").getString("id");
                                    crossSiteDbHealthy = crossSiteHealthRequest(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:database-health");
                                    crossSiteAdminHealthy = crossSiteHealthRequest(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:admin-health");
                                    performedCrossSiteHealthCheck = true;
                                } catch(Exception e) {
                                    log.info("Cannot get site identifier from {}", clusterActor.getNode());
                                    log.error("Site Health Error", e);
                                }
                            }
                        }
                    }
                }
                SitesBuilder builder = getSitesBuilder(site1HealthyODLs, site1Voting, HEALTHY.equals(adminHealth), HEALTHY.equals(databaseHealth), siteIdentifier);
                outputBuilder.getSites().add(builder.build());
                builder = getSitesBuilder(site2HealthyODLs, site2Voting, crossSiteAdminHealthy, crossSiteDbHealthy, crossSiteIdentifier);
                outputBuilder.getSites().add(builder.build());
            }
            else {
                // Make calls over to site 1 healthchecks
                for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
                    ClusterActor clusterActor = entry.getValue();
                    if(clusterActor.isUp() && !clusterActor.isUnreachable()) {
                        if(ClusterActor.SITE_1.equals(clusterActor.getSite())) {
                            site1HealthyODLs++;
                            if(clusterActor.isVoting()) {
                                site1Voting = true;
                            }
                            if(!performedCrossSiteHealthCheck) {
                                try {
                                    String content = getRequestContent(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:site-identifier", HttpMethod.POST);
                                    crossSiteIdentifier = new JSONObject(content).getJSONObject("output").getString("id");
                                    crossSiteDbHealthy = crossSiteHealthRequest(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:database-health");
                                    crossSiteAdminHealthy = crossSiteHealthRequest(httpProtocol + clusterActor.getNode() + ":" + port + "/restconf/operations/gr-toolkit:admin-health");
                                    performedCrossSiteHealthCheck = true;
                                } catch(Exception e) {
                                    log.info("Cannot get site identifier from {}", clusterActor.getNode());
                                    log.error("Site Health Error", e);
                                }
                            }
                        }
                        else {
                            site2HealthyODLs++;
                            if(clusterActor.isVoting()) {
                                site2Voting = true;
                            }
                        }
                    }
                }
                // Build Output
                SitesBuilder builder = getSitesBuilder(site1HealthyODLs, site1Voting, crossSiteAdminHealthy, crossSiteDbHealthy, crossSiteIdentifier);
                outputBuilder.getSites().add(builder.build());
                builder = getSitesBuilder(site2HealthyODLs, site2Voting, HEALTHY.equals(adminHealth), HEALTHY.equals(databaseHealth), siteIdentifier);
                outputBuilder.getSites().add(builder.build());
            }
        }

        RpcResult<SiteHealthOutput> rpcResult = RpcResultBuilder.<SiteHealthOutput>status(true).withResult(outputBuilder.build()).build();
        return Futures.immediateFuture(rpcResult);
    }

    private SitesBuilder getSitesBuilder(int siteHealthyODLs, boolean siteVoting, boolean adminHealthy, boolean dbHealthy, String siteIdentifier) {
        SitesBuilder builder = new SitesBuilder();
        if(siteHealthyODLs > 1) {
            builder.setHealth(HEALTHY);
        }
        else {
            log.warn("{} Healthy ODLs: {}", siteIdentifier, siteHealthyODLs);
            builder.setHealth(FAULTY);
        }
        if(!adminHealthy) {
            log.warn("{} Admin Health: {}", siteIdentifier, FAULTY);
            builder.setHealth(FAULTY);
        }
        if(!dbHealthy) {
            log.warn("{} Database Health: {}", siteIdentifier, FAULTY);
            builder.setHealth(FAULTY);
        }
        if(siteVoting) {
            builder.setRole("ACTIVE");
        }
        else {
            builder.setRole("STANDBY");
        }
        builder.setId(siteIdentifier);
        return builder;
    }

    private boolean isSite1() {
        int memberNumber = Integer.parseInt(member.split("-")[1]);
        boolean isSite1 = memberNumber < 4;
        log.info("isSite1(): {}", isSite1);
        return isSite1;
    }

    private void parseSeedNodes(String line) {
        memberMap = new HashMap<>();
        line = line.substring(line.indexOf("[\""), line.indexOf(']'));
        String[] splits = line.split(",");

        for(int ndx = 0; ndx < splits.length; ndx++) {
            String nodeName = splits[ndx];
            int delimLocation = nodeName.indexOf('@');
            String port = nodeName.substring(splits[ndx].indexOf(':', delimLocation) + 1, splits[ndx].indexOf('"', splits[ndx].indexOf(':')));
            splits[ndx] = nodeName.substring(delimLocation + 1, splits[ndx].indexOf(':', delimLocation));
            log.info("Adding node: {}:{}", splits[ndx], port);
            ClusterActor clusterActor = new ClusterActor();
            clusterActor.setNode(splits[ndx]);
            clusterActor.setAkkaPort(port);
            clusterActor.setMember("member-" + (ndx + 1));
            if(ndx < 3) {
                clusterActor.setSite(ClusterActor.SITE_1);
            }
            else {
                clusterActor.setSite(ClusterActor.SITE_2);
            }

            if(member.equals(clusterActor.getMember())) {
                self = clusterActor;
            }
            memberMap.put(clusterActor.getNode(), clusterActor);
            log.info("{}", clusterActor);
        }

        if(memberMap.size() == 1) {
            log.info("1 member found. This is a solo environment.");
            siteConfiguration = SiteConfiguration.SOLO;
        }
        else if(memberMap.size() == 3) {
            log.info("This is a single site.");
            siteConfiguration = SiteConfiguration.SINGLE;
        }
        else if(memberMap.size() == 6) {
            log.info("This is a georedundant site.");
            siteConfiguration = SiteConfiguration.GEO;
        }
    }

    private void getMemberStatus(ClusterActor clusterActor) throws IOException {
        log.info("Getting member status for {}", clusterActor.getNode());
        String content = getRequestContent(httpProtocol + clusterActor.getNode() + jolokiaClusterPath, HttpMethod.GET);
        try {
            JSONObject responseJson = new JSONObject(content);
            JSONObject responseValue = responseJson.getJSONObject(VALUE);
            clusterActor.setUp("Up".equals(responseValue.getString("MemberStatus")));
            clusterActor.setUnreachable(false);
        } catch(JSONException e) {
            log.error("Error parsing response from {}", clusterActor.getNode(), e);
            clusterActor.setUp(false);
            clusterActor.setUnreachable(true);
        }
    }

    private void getShardStatus(ClusterActor clusterActor) throws IOException {
        log.info("Getting shard status for {}", clusterActor.getNode());
        String content = getRequestContent(httpProtocol + clusterActor.getNode() + shardManagerPath, HttpMethod.GET);
        try {
            JSONObject responseValue = new JSONObject(content).getJSONObject(VALUE);
            JSONArray shardList = responseValue.getJSONArray("LocalShards");

            String pattern = "-config$";
            Pattern r = Pattern.compile(pattern);
            Matcher m;
            for(int ndx = 0; ndx < shardList.length(); ndx++) {
                String configShardName = shardList.getString(ndx);
                m = r.matcher(configShardName);
                String operationalShardName = m.replaceFirst("-operational");
                String shardConfigPath = String.format(shardPathTemplate, configShardName);
                String shardOperationalPath = String.format(shardPathTemplate, operationalShardName).replace("Config", "Operational");
                extractShardInfo(clusterActor, configShardName, shardConfigPath);
                extractShardInfo(clusterActor, operationalShardName, shardOperationalPath);
            }
        } catch(JSONException e) {
            log.error("Error parsing response from " + clusterActor.getNode(), e);
        }
    }

    private void extractShardInfo(ClusterActor clusterActor, String shardName, String shardPath) throws IOException {
        log.info("Extracting shard info for {}", shardName);
        log.debug("Pulling config info for {} from: {}", shardName, shardPath);
        String content = getRequestContent(httpProtocol + clusterActor.getNode() + shardPath, HttpMethod.GET);
        log.debug("Response: {}", content);

        try {
            JSONObject shardValue = new JSONObject(content).getJSONObject(VALUE);
            clusterActor.setVoting(shardValue.getBoolean("Voting"));
            if(shardValue.getString("PeerAddresses").length() > 0) {
                clusterActor.getReplicaShards().add(shardName);
                if(shardValue.getString("Leader").startsWith(clusterActor.getMember())) {
                    clusterActor.getShardLeader().add(shardName);
                }
            }
            else {
                clusterActor.getNonReplicaShards().add(shardName);
            }
            JSONArray followerInfo = shardValue.getJSONArray("FollowerInfo");
            for(int followerNdx = 0; followerNdx < followerInfo.length(); followerNdx++) {
                int commitIndex = shardValue.getInt("CommitIndex");
                int matchIndex = followerInfo.getJSONObject(followerNdx).getInt("matchIndex");
                if(commitIndex != -1 && matchIndex != -1) {
                    int commitsBehind = commitIndex - matchIndex;
                    clusterActor.getCommits().put(followerInfo.getJSONObject(followerNdx).getString("id"), commitsBehind);
                }
            }
        } catch(JSONException e) {
            log.error("Error parsing response from " + clusterActor.getNode(), e);
        }
    }

    private void getControllerHealth() {
        for(Map.Entry<String, ClusterActor> entry : memberMap.entrySet()) {
            ClusterActor clusterActor = entry.getValue();
            String key = entry.getKey();
            try {
                // First flush out the old values
                clusterActor.flush();
                log.info("Gathering info for {}", clusterActor.getNode());
                getMemberStatus(clusterActor);
                getShardStatus(clusterActor);
                log.info("MemberInfo:\n{}", clusterActor);
            } catch(IOException e) {
                log.error("Connection Error", e);
                memberMap.get(key).setUnreachable(true);
                memberMap.get(key).setUp(false);
                log.info("MemberInfo:\n{}", memberMap.get(key));
            }
        }
    }

    private void modifyIpTables(IpTables task, Object[] nodeInfo) {
        log.info("Modifying IPTables rules...");
        if(task == IpTables.ADD) {
            for(Object node : nodeInfo) {
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.halt.akka.traffic.input.NodeInfo n =
                        (org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.halt.akka.traffic.input.NodeInfo) node;
                log.info("Isolating {}", n.getNode());
                executeCommand(String.format("sudo /sbin/iptables -A INPUT -p tcp --destination-port %s -j DROP -s %s", properties.get(PropertyKeys.CONTROLLER_PORT_AKKA), n.getNode()));
                executeCommand(String.format("sudo /sbin/iptables -A OUTPUT -p tcp --destination-port %s -j DROP -s %s", n.getPort(), n.getNode()));
            }

        } else if(task == IpTables.DELETE) {
            for(Object node : nodeInfo) {
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.resume.akka.traffic.input.NodeInfo n =
                        (org.opendaylight.yang.gen.v1.org.onap.ccsdk.sli.plugins.gr.toolkit.rev180926.resume.akka.traffic.input.NodeInfo) node;
                log.info("De-isolating {}", n.getNode());
                executeCommand(String.format("sudo /sbin/iptables -D INPUT -p tcp --destination-port %s -j DROP -s %s", properties.get(PropertyKeys.CONTROLLER_PORT_AKKA), n.getNode()));
                executeCommand(String.format("sudo /sbin/iptables -D OUTPUT -p tcp --destination-port %s -j DROP -s %s", n.getPort(), n.getNode()));
            }

        }
        executeCommand("sudo /sbin/iptables -L");
    }

    private void executeCommand(String command) {
        log.info("Executing command: {}", command);
        String[] cmd = command.split(" ");
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while((inputLine = bufferedReader.readLine()) != null) {
                content.append(inputLine);
            }
            bufferedReader.close();
            log.info("{}", content);
        } catch(IOException e) {
            log.error("Error executing command", e);
        }
    }

    private boolean crossSiteHealthRequest(String path) throws IOException {
        String content = getRequestContent(path, HttpMethod.POST);
        try {
            JSONObject responseJson = new JSONObject(content);
            JSONObject responseValue = responseJson.getJSONObject(VALUE);
            return HEALTHY.equals(responseValue.getString("health"));
        } catch(JSONException e) {
            log.error("Error parsing JSON", e);
            throw new IOException();
        }
    }

    private String getAdminHealth() {
        String protocol = "true".equals(properties.getProperty(PropertyKeys.ADM_USE_SSL)) ? "https://" : "http://";
        String port = "true".equals(properties.getProperty(PropertyKeys.ADM_USE_SSL)) ? properties.getProperty(PropertyKeys.ADM_PORT_SSL) : properties.getProperty(PropertyKeys.ADM_PORT_HTTP);
        String path = protocol + properties.getProperty(PropertyKeys.ADM_FQDN) + ":" + port + properties.getProperty(PropertyKeys.ADM_HEALTHCHECK);
        log.info("Requesting healthcheck from {}", path);
        try {
            int response = getRequestStatus(path, HttpMethod.GET);
            log.info("Response: {}", response);
            if(response == 200)
                return HEALTHY;
            return FAULTY;
        } catch(IOException e) {
            log.error("Problem getting ADM health.", e);
            return FAULTY;
        }
    }

    private String getDatabaseHealth() {
        log.info("Determining database health...");
        try {
            log.info("DBLib isActive(): {}", dbLib.isActive());
            log.info("DBLib isReadOnly(): {}", dbLib.getConnection().isReadOnly());
            log.info("DBLib isClosed(): {}", dbLib.getConnection().isClosed());
            if(!dbLib.isActive() || dbLib.getConnection().isClosed() || dbLib.getConnection().isReadOnly()) {
                log.warn("Database is FAULTY");
                return FAULTY;
            }
            log.info("Database is HEALTHY");
        } catch(SQLException e) {
            log.error("Database is FAULTY");
            log.error("Error", e);
            return FAULTY;
        }

        return HEALTHY;
    }

    private String getRequestContent(String path, HttpMethod method) throws IOException {
        return getRequestContent(path, method, null);
    }

    private String getRequestContent(String path, HttpMethod method, String input) throws IOException {
        HttpURLConnection connection = getConnection(path);
        connection.setRequestMethod(method.getMethod());
        connection.setDoInput(true);

        if(input != null) {
            sendPayload(input, connection);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = bufferedReader.readLine()) != null) {
            content.append(inputLine);
        }
        bufferedReader.close();
        connection.disconnect();
        return content.toString();
    }

    private int getRequestStatus(String path, HttpMethod method) throws IOException {
        return getRequestStatus(path, method, null);
    }

    private int getRequestStatus(String path, HttpMethod method, String input) throws IOException {
        HttpURLConnection connection = getConnection(path);
        connection.setRequestMethod(method.getMethod());
        connection.setDoInput(true);

        if(input != null) {
            sendPayload(input, connection);
        }
        int response = connection.getResponseCode();
        log.info("Received {} response code from {}", response, path);
        connection.disconnect();
        return response;
    }

    private void sendPayload(String input, HttpURLConnection connection) throws IOException {
        byte[] out = input.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        connection.setFixedLengthStreamingMode(length);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.connect();
        try(OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }
    }

    private HttpURLConnection getConnection(String host) throws IOException {
        log.info("Getting connection to: {}", host);
        URL url = new URL(host);
        String auth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(credentials.getBytes());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Authorization", auth);
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Proxy-Connection", "keep-alive");
        return connection;
    }

    private enum IpTables {
        ADD,
        DELETE
    }

    private enum SiteConfiguration {
        SOLO,
        SINGLE,
        GEO
    }

    private enum HttpMethod {
        GET("GET"),
        POST("POST");

        private String method;
        HttpMethod(String method) {
            this.method = method;
        }
        public String getMethod() {
            return method;
        }
    }

    private class PropertyKeys {
        static final String SITE_IDENTIFIER = "site.identifier";
        static final String CONTROLLER_USE_SSL = "controller.useSsl";
        static final String CONTROLLER_PORT_SSL = "controller.port.ssl";
        static final String CONTROLLER_PORT_HTTP = "controller.port.http";
        static final String CONTROLLER_PORT_AKKA = "controller.port.akka";
        static final String CONTROLLER_CREDENTIALS = "controller.credentials";
        static final String AKKA_CONF_LOCATION = "akka.conf.location";
        static final String MBEAN_CLUSTER = "mbean.cluster";
        static final String MBEAN_SHARD_MANAGER  = "mbean.shardManager";
        static final String MBEAN_SHARD_CONFIG = "mbean.shard.config";
        static final String ADM_USE_SSL = "adm.useSsl";
        static final String ADM_PORT_SSL = "adm.port.ssl";
        static final String ADM_PORT_HTTP = "adm.port.http";
        static final String ADM_FQDN = "adm.fqdn";
        static final String ADM_HEALTHCHECK= "adm.healthcheck";
    }
}