/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.renren.finance.service.locator.factory.Node;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:25
 */
public class DefaultServiceDiscoverer implements IServiceDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceDiscoverer.class);

    private ServiceDiscovery<NodeInstanceDetail> serviceDiscovery;

    private static final String SERVICE_HOME_PATH = "/finance/service";
    private static final String ZK_CONNECT_STRING = "10.3.24.123:12181";

    private Map<String, ServiceProvider<NodeInstanceDetail>> providerMap = new ConcurrentHashMap<String, ServiceProvider<NodeInstanceDetail>>();

    private static class DiscovererHolder {

        private static final DefaultServiceDiscoverer instance = getDiscoverer();

        private static DefaultServiceDiscoverer getDiscoverer() {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(ZK_CONNECT_STRING)
                    .retryPolicy(retryPolicy)
                    .build();
            client.start();

            DefaultServiceDiscoverer discoverer = null;
            try {
                discoverer = new DefaultServiceDiscoverer(client, SERVICE_HOME_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("DefaultServiceDiscoverer.DiscovererHolder.getDiscoverer", e);
            }
            return discoverer;
        }
    }

    private DefaultServiceDiscoverer(CuratorFramework client, String basePath) throws Exception {
        serviceDiscovery = ServiceDiscoveryBuilder.builder(NodeInstanceDetail.class)
                .client(client)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public static IServiceDiscoverer getInstance() {
        return DiscovererHolder.instance;
    }

    public ServiceInstance<NodeInstanceDetail> getService(String serviceName) throws Exception {
        ServiceProvider<NodeInstanceDetail> provider = providerMap.get(serviceName);
        if (provider == null) {
            provider = serviceDiscovery.serviceProviderBuilder()
                    .serviceName(serviceName)
                    .providerStrategy(new RandomStrategy<NodeInstanceDetail>())
                    .build();
            provider.start();
            providerMap.put(serviceName, provider);
        }
        return provider.getInstance();
    }

    @Override
    public Node getNode(String serviceId) {
        ServiceNode node = null;
        try {
            ServiceInstance<NodeInstanceDetail> service = getService(serviceId);
            node = new ServiceNode();
            node.setHost(service.getAddress());
            node.setPort(service.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("DefaultServiceDiscoverer.getNode failed for " + serviceId, e);
        }
        return node;
    }

    public synchronized void close() {
        for (ServiceProvider<NodeInstanceDetail> provider : providerMap.values()) {
            CloseableUtils.closeQuietly(provider);
        }
    }
}
