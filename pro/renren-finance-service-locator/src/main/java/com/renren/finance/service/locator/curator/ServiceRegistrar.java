/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:01
 */
public class ServiceRegistrar {

    private ServiceDiscovery<NodeInstanceDetail> serviceDiscovery;
    private final CuratorFramework client;

    public ServiceRegistrar(CuratorFramework client, String basePath) throws Exception {
        this.client = client;
        serviceDiscovery = ServiceDiscoveryBuilder.builder(NodeInstanceDetail.class)
                .client(client)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public void register(ServiceInstance<NodeInstanceDetail> instance) throws Exception {
        serviceDiscovery.registerService(instance);
    }

    public void unregister(ServiceInstance<NodeInstanceDetail> instance) throws Exception {
        serviceDiscovery.unregisterService(instance);
    }

    public void updateService(ServiceInstance<NodeInstanceDetail> instance) throws Exception {
        serviceDiscovery.updateService(instance);
    }

    public void close() throws Exception {
        serviceDiscovery.close();
    }

}
