/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerTransport;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-30 下午6:41
 */
public class ThriftServer implements Runnable {

    private TServer tServer;

    private TServerTransport tServerTransport;

    private TProcessor tProcessor;


    @Override
    public void run() {
        System.out.println("server begin ---------------");
        tServer.serve();
        System.out.println("-----------------------------");
        tServer.stop();
    }

    public TServer gettServer() {
        return tServer;
    }

    public void settServer(TServer tServer) {
        this.tServer = tServer;
    }

    public TServerTransport gettServerTransport() {
        return tServerTransport;
    }

    public void settServerTransport(TServerTransport tServerTransport) {
        this.tServerTransport = tServerTransport;
    }

    public TProcessor gettProcessor() {
        return tProcessor;
    }

    public void settProcessor(TProcessor tProcessor) {
        this.tProcessor = tProcessor;
    }
}
