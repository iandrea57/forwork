/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import com.renren.finance.service.locator.curator.DefaultServiceRegistrar;
import com.renren.finance.service.locator.curator.IServiceRegistrar;
import com.renren.finance.service.locator.factory.ClassDefinition;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.net.InetSocketAddress;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-28 下午5:20
 */
public class RegisterFactory {

    private static IServiceRegistrar serviceRegistrar = DefaultServiceRegistrar.getInstance();

    public static <T> void regist(Class<T> serviceInterface, T serviceImpl, RegisterInfo info) {
        try {
            ServerDefinition serverDefinition = new ServerDefinition(serviceInterface);
            String serviceId = serverDefinition.getServiceId();
            serviceRegistrar.register(serviceId, info);

            Object processor = serverDefinition.getServiceProcessorConstructor().newInstance(serviceImpl);
            TServerTransport serverTransport = new TServerSocket(new InetSocketAddress("0.0.0.0", info.getNode().getPort()));
            TThreadPoolServer.Args trArgs = new TThreadPoolServer.Args(serverTransport);
            trArgs.processor((TProcessor) processor);
            trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
            trArgs.transportFactory(new TTransportFactory());
            TServer server = new TThreadPoolServer(trArgs);
            System.out.println("server begin ---------------");
            server.serve();
            System.out.println("-----------------------------");
            server.stop();
            serviceRegistrar.unregister(serviceId, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
