/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.io.Closeable;
import java.util.List;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:25
 */
public class ServiceDiscoverer {

    private ServiceDiscovery<InstanceDetail> serviceDiscovery;

    private List<Closeable> closeableList = Lists.newArrayList();

    public ServiceDiscoverer(CuratorFramework client, String basePath) throws Exception {
        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetail.class)
                .client(client)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public ServiceInstance<InstanceDetail> getInstanceByName(String serviceName) throws Exception {
        ServiceProvider<InstanceDetail> provider = serviceDiscovery.serviceProviderBuilder()
                .serviceName(serviceName)
                .providerStrategy(new RandomStrategy<InstanceDetail>())
                .build();
        provider.start();
        closeableList.add(provider);
        return provider.getInstance();
    }

    public synchronized void close() {
        for (Closeable closeable : closeableList) {
            CloseableUtils.closeQuietly(closeable);
        }
    }

}
