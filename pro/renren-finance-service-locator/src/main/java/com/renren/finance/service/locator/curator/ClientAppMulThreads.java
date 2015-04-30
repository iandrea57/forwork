/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.service.api.GetInfoRequest;
import com.mmz.service.api.GetInfoResponse;
import com.renren.finance.service.locator.factory.ServiceFactory;
import org.apache.thrift.TException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-29 下午1:31
 */
public class ClientAppMulThreads {

    public static void main(String[] args) {

        final IInfoService infoService = ServiceFactory.getService(IInfoService.class, 3000);

        ExecutorService execService = new ThreadPoolExecutor(5, 10,
                30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(20),
                new ThreadPoolExecutor.CallerRunsPolicy());
        final AtomicInteger it = new AtomicInteger();
        for (int i= 0; i< 10000; i++) {
            execService.execute(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();

                    GetInfoRequest req = new GetInfoRequest();
                    req.setUid(111);
                    GetInfoResponse resp = null;
                    try {
                        resp = infoService.getInfo(req);
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                    System.out.println(it.incrementAndGet() + " total used : " + (System.currentTimeMillis()-start));
                }
            });
        }
    }
}
