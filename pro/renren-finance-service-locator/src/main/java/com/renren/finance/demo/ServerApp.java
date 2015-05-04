/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.demo;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.frameworkuse.thrift.InfoServiceImpl;
import com.renren.finance.service.locator.register.RegisterInfo;
import com.renren.finance.service.locator.register.ServicePublisher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:07
 */
public class ServerApp {

    public static void main(String[] args) throws Exception {
        List<String> alarm = new ArrayList<String>();
        alarm.add("18612276357");
        alarm.add("13699441057");

        RegisterInfo info = new RegisterInfo.Builder().port(9813).alarmPhones(alarm).build();
        ServicePublisher.publish(IInfoService.class, new InfoServiceImpl(), info);

        System.out.println("finish");
    }
}
