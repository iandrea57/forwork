/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

import java.util.UUID;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:07
 */
public class ServerApp {

    public static void main(String[] args) throws Exception {
        String zkConnectString = "10.3.24.123:12181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkConnectString)
                .retryPolicy(retryPolicy)
//                .connectionTimeoutMs(1000)
                .sessionTimeoutMs(1000)
                .build();
        client.start();

        String myPath = "/service/lbs/search";
        ServiceRegistrar registrar = new ServiceRegistrar(client, myPath);
        ServiceInstance<InstanceDetail> instance1 = ServiceInstance.<InstanceDetail>builder()
                .name("service1")
                .port(12345)
                .address("192.168.1.100")   //address不写的话,会取本地ip
                .payload(new InstanceDetail(UUID.randomUUID().toString(),"192.168.1.100",12345,"Test.Service1"))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        ServiceInstance<InstanceDetail> instance2 = ServiceInstance.<InstanceDetail>builder()
                .name("service1")
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        registrar.register(instance1);
        registrar.register(instance2);
        System.out.println("start");

//        Thread.sleep(5000);

//        CloseableUtils.closeQuietly(client);
        System.out.println("finish");
    }
}
