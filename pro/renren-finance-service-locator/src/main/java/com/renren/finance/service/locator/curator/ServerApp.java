/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.mmz.frameworkuse.thrift.IInfoService;
import com.mmz.frameworkuse.thrift.InfoServiceImpl;
import com.renren.finance.service.locator.register.RegisterInfo;
import com.renren.finance.service.locator.registrar.ServicePublisher;
import com.renren.finance.service.locator.util.IpUtils;

import java.util.ArrayList;
import java.util.List;

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

        String localIp = IpUtils.getOneLocalIp();

        RegisterInfo info = new RegisterInfo();
        info.setInterfaceName(IInfoService.class.getCanonicalName());
        ServiceNode node = new ServiceNode();
        node.setHost(localIp);
        node.setPort(9813);
        info.setNode(node);
        info.setPhones(alarm);

        ServicePublisher.publish(IInfoService.class, new InfoServiceImpl(), info);

        System.out.println("finish");
    }
}
