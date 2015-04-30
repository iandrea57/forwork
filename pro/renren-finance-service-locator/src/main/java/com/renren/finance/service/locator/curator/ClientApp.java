/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.service.api.GetInfoRequest;
import com.mmz.service.api.GetInfoResponse;
import com.renren.finance.service.locator.factory.ServiceFactory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:42
 */
public class ClientApp {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();


        IInfoService infoService = ServiceFactory.getService(IInfoService.class, 3000);


        GetInfoRequest req = new GetInfoRequest();
        req.setUid(111);

        GetInfoResponse resp = infoService.getInfo(req);

        System.out.println(resp);
        System.out.println("total used : " + (System.currentTimeMillis()-start));
        System.out.println("-------------------------");
    }
}
