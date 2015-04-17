/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;

import java.util.Collection;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午2:30
 */
public class CuratorTest {

    public void clientTest() throws Exception {
        String zkConnectString = "10.3.24.123:12181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkConnectString)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        String myPath = "/service/lbs/search";
//        client.create().forPath(myPath);


        ServiceDiscovery discovery = ServiceDiscoveryBuilder.builder(CuratorTest.class)
                .client(client)
                .basePath(myPath)
                .build();

        String serviceName = "poi-search";
        Collection<String> names = discovery.queryForNames();
        for (String name : names) {
            System.out.println(name);
        }


        ServiceProvider provider = discovery.serviceProviderBuilder()
                .serviceName(serviceName)
                .build();


        client.close();
    }

    public static void main(String[] args) throws Exception {
        CuratorTest test = new CuratorTest();
        test.clientTest();

        System.exit(0);
    }


}
