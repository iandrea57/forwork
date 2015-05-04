/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import com.renren.finance.service.locator.registrar.DefaultNodeRegistrar;
import com.renren.finance.service.locator.registrar.INodeRegistrar;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-30 下午4:59
 */
public class ServicePublisher {

    private static INodeRegistrar serviceRegistrar = DefaultNodeRegistrar.getInstance();

    public static <T> void publish(Class<T> serviceInterface, T serviceImpl, RegisterInfo info) {
        try {
            info.setInterfaceName(serviceInterface.getName());

            ServerClassDefinition serviceDefinition = new ServerClassDefinition(serviceInterface);
            String serviceId = serviceDefinition.getServiceId();
            serviceRegistrar.register(serviceId, info);

            TProcessor processor = (TProcessor)serviceDefinition.getServiceProcessorConstructor().newInstance(serviceImpl);
            TServerTransport serverTransport = new TNonblockingServerSocket(info.getNode().getPort());
            TThreadedSelectorServer.Args trArgs = new TThreadedSelectorServer.Args((TNonblockingServerTransport)serverTransport);
            trArgs.processor(processor);
            trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
            trArgs.transportFactory(new TFramedTransport.Factory());
            trArgs.executorService(createExecuteService(info.getCoreSize(), info.getMaxSize()));
            if (info.getSelectorThreads() > 0) {
                trArgs.selectorThreads(info.getSelectorThreads());
            }
            TServer server = new TThreadedSelectorServer(trArgs);

            ThriftServer service = new ThriftServer();
            service.settProcessor(processor);
            service.settServerTransport(serverTransport);
            service.settServer(server);

            Thread thread = new Thread(service, serviceId);
            thread.start();

//            serviceRegistrar.unregister(serviceId, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ExecutorService createExecuteService(int coreSize, int maxSize) {
        if (coreSize <= 0 || maxSize <= 0) {
            coreSize = 2 * Runtime.getRuntime().availableProcessors();
            maxSize = 2 * coreSize;
        }
        return new ThreadPoolExecutor(coreSize, maxSize, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ServiceThreadFactory());
    }

}
