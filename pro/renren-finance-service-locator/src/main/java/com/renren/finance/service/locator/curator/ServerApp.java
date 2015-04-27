/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.frameworkuse.thrift.InfoServiceImpl;
import com.mmz.xt.service.api.InfoService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:07
 */
public class ServerApp {
    private static final String SERVICE_HOME_PATH = "/finance/service";
    private static final String ZK_CONNECT_STRING = "10.3.24.123:12181";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ZK_CONNECT_STRING)
                .retryPolicy(retryPolicy)
//                .connectionTimeoutMs(1000)
                .sessionTimeoutMs(1000)
                .build();
        client.start();

        ServiceRegistrar registrar = new ServiceRegistrar(client, SERVICE_HOME_PATH);

        List<String> alarm = new ArrayList<String>();
        alarm.add("18612276357");
        alarm.add("13699441057");

        ServiceInstance<NodeInstanceDetail> instance1 = ServiceInstance.<NodeInstanceDetail>builder()
                .name("infoservice")
                .port(9813)
                .address("127.0.0.1")   //address不写的话,会取本地ip
                .payload(new NodeInstanceDetail(UUID.randomUUID().toString(),"192.168.1.100",12345,"Test.infoservice", alarm))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        registrar.register(instance1);

        InfoService.Processor<IInfoService> processor = new InfoService.Processor<IInfoService>(
                new InfoServiceImpl());
        TServerTransport serverTransport = new TServerSocket(
                new InetSocketAddress("0.0.0.0", 9813));
        TThreadPoolServer.Args trArgs = new TThreadPoolServer.Args(serverTransport);
        trArgs.processor(processor);
        trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
        trArgs.transportFactory(new TTransportFactory());
        TServer server = new TThreadPoolServer(trArgs);
        System.out.println("server begin ---------------");
        server.serve();
        System.out.println("-----------------------------");
        server.stop();
//
//        ServiceInstance<NodeInstanceDetail> instance2 = ServiceInstance.<NodeInstanceDetail>builder()
//                .name("service1")
//                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
//                .build();
//        registrar.register(instance2);
//        System.out.println("start");

//        Thread.sleep(5000);

//        CloseableUtils.closeQuietly(client);
        System.out.println("finish");
    }
}
