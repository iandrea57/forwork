/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-29 上午11:43
 */
public class IpUtils {

    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 使用 {@code java.net.InetAddress.isSiteLocalAddress()} 获取内网IP
     *
     * @return 内网IP列表
     */
    public static List<String> getLocalIps() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> addrs = ni.getInetAddresses();

                while (addrs.hasMoreElements()) {
                    InetAddress inetAddress = addrs.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }

        } catch (SocketException e) {
            logger.error("", e);
        }

        return ipList;
    }

    public static String getOneLocalIp() {
        List<String> localIps = getLocalIps();
        return localIps.isEmpty() ? null : localIps.get(0);
    }
}
