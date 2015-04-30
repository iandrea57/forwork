/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.frameworkuse.thrift;

import com.mmz.service.api.GetInfoRequest;
import com.mmz.service.api.GetInfoResponse;
import com.mmz.service.api.InfoService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-10 下午12:14
 */
public class InfoClient {

    public static void main(String[] args) throws TException {
        TTransport transport = new TSocket("127.0.0.1", 9813);
        long start = System.currentTimeMillis();
        TProtocol protocol = new TBinaryProtocol(transport);
        InfoService.Client client = new InfoService.Client(protocol);
        transport.open();

        GetInfoRequest req = new GetInfoRequest();
        req.setUid(111);

        GetInfoResponse resp = client.getInfo(req);

        transport.close();
        System.out.println(resp);
        System.out.println("total used : " + (System.currentTimeMillis()-start));
        System.out.println("-------------------------");
    }
}
