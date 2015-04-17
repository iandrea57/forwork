/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.frameworkuse.thrift;

import com.mmz.xt.service.api.InfoService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.net.InetSocketAddress;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-10 下午12:04
 */
public class InfoServer {

    public static void main(String[] args) throws Exception {
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
    }
}
