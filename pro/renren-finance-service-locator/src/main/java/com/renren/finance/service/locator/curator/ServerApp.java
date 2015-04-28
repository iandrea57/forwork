/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.frameworkuse.thrift.InfoServiceImpl;
import com.mmz.xt.service.api.InfoService;
import com.renren.finance.service.locator.factory.Node;
import com.renren.finance.service.locator.register.RegisterFactory;
import com.renren.finance.service.locator.register.RegisterInfo;
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
        List<String> alarm = new ArrayList<String>();
        alarm.add("18612276357");
        alarm.add("13699441057");

        RegisterInfo info = new RegisterInfo();
        info.setInterfaceName(IInfoService.class.getCanonicalName());
        ServiceNode node = new ServiceNode();
        node.setHost("10.2.52.74");
        node.setPort(9813);
        info.setNode(node);
        info.setPhones(alarm);

        RegisterFactory.regist(IInfoService.class, new InfoServiceImpl(), info);

        System.out.println("finish");
    }
}
