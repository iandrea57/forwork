/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-30 下午5:10
 */
public class LocatorConf {

    private static final Logger logger = LoggerFactory.getLogger(LocatorConf.class);

    private static final String LOCATOR_CONF_PROP = "finance.service.locator.config";
    private static final String DEFAULT_CONF_FILE = "finance_service_locator.properties";

    private static final String CLUSTER_FILED = "cluster";
    private static final String BASE_PATH_FILED = "basePath";
    private static final String SERVER_ALARM_PHONES = "server.alarm.phones";
    private static final String SERVER_ALARM_EMAILS = "server.alarm.emails";

    private String cluster;

    private String basePath;

    private List<String> serverAlarmPhones;

    private List<String> serverAlarmEmails;

    public static LocatorConf instance() {
        return LocatorConfHolder.instance;
    }

    private LocatorConf() {}

    private static class LocatorConfHolder {

        private static final LocatorConf instance = init();

        private static LocatorConf init() {
            try {
                return get();
            } catch (InvalidLocatorConfException e) {
                throw new IllegalStateException("Invalid finance locator configuration", e);
            }
        }

        public static LocatorConf get() throws InvalidLocatorConfException {
            String confPath = System.getProperty(LOCATOR_CONF_PROP);
            if (confPath == null) {
                confPath = DEFAULT_CONF_FILE;
            }
            File confFile = new File(LocatorConf.class.getResource("/").getPath() + confPath);
            if (logger.isDebugEnabled()) {
                logger.debug("finance.service.locator.config file: {}", confFile.getPath());
            }
            Reader reader;
            try {
                if (confFile.exists()) {
                    reader = new InputStreamReader(new FileInputStream(confFile), "UTF-8");
                } else {
                    reader = new InputStreamReader(LocatorConf.class.getResourceAsStream("/" + DEFAULT_CONF_FILE));
                }
                Properties props = new Properties();
                props.load(reader);
                return get(props);
            } catch (IOException e) {
                throw new InvalidLocatorConfException("can't load finance locator config", e);
            }
        }

        private static LocatorConf get(Properties props) throws InvalidLocatorConfException {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Loaded config: [ ");
                for (Map.Entry<?, ?> e : props.entrySet()) {
                    sb.append(e.getKey()).append(":").append(e.getValue()).append(", ");
                }
                sb.append("]");
                logger.debug(sb.toString());
            }

            if (!props.containsKey(CLUSTER_FILED)) {
                throw new InvalidLocatorConfException("Config missed field '" + CLUSTER_FILED + "'");
            }
            if (!props.containsKey(BASE_PATH_FILED)) {
                throw new InvalidLocatorConfException("Config missed field '" + BASE_PATH_FILED + "'");
            }

            LocatorConf conf = new LocatorConf();
            conf.cluster = props.getProperty(CLUSTER_FILED);
            conf.basePath = props.getProperty(BASE_PATH_FILED);
            conf.serverAlarmPhones = tokenizeToStringList(props.getProperty(SERVER_ALARM_PHONES));
            conf.serverAlarmEmails = tokenizeToStringList(props.getProperty(SERVER_ALARM_EMAILS));
            return conf;
        }

        private static List<String> tokenizeToStringList(String str) {
            List<String> tokens = Collections.emptyList();
            if (str == null) {
                return tokens;
            }
            StringTokenizer st = new StringTokenizer(str, ",");
            tokens = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                tokens.add(st.nextToken());
            }
            return tokens;
        }
    }

    public String getCluster() {
        return cluster;
    }

    public String getBasePath() {
        return basePath;
    }

    public List<String> getServerAlarmPhones() {
        return serverAlarmPhones;
    }

    public List<String> getServerAlarmEmails() {
        return serverAlarmEmails;
    }

    @Override
    public String toString() {
        return "LocatorConf{" +
                "cluster='" + cluster + '\'' +
                ", basePath='" + basePath + '\'' +
                ", serverAlarmPhones=" + serverAlarmPhones +
                ", serverAlarmEmails=" + serverAlarmEmails +
                "} ";
    }

    public static void main(String[] args) {
        LocatorConf conf = LocatorConf.instance();
        System.out.println(conf);
    }
}
