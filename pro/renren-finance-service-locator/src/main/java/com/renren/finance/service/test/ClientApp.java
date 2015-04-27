/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:42
 */
public class ClientApp {

    public static void main(String[] args) throws Exception {
        String zkConnectString = "10.3.24.123:12181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkConnectString)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        String myPath = "/finance/service";
        ServiceDiscoverer discoverer = new ServiceDiscoverer(client, myPath);

        for (int i = 0; i < 10; i++) {
            ServiceInstance<InstanceDetail> instance = discoverer.getInstanceByName("service1");
            System.out.println(instance.buildUriSpec());
            System.out.println(instance.getPayload());
        }

        discoverer.close();
        CloseableUtils.closeQuietly(client);

    }
}
