/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.forwork.network.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-13 下午2:37
 */
public class InetSocketAddressU {

    public static void main(String[] args) throws IOException {
        Socket socket1 = new Socket("www.baidu.com", 80);
        SocketAddress socketAddress = socket1.getRemoteSocketAddress();
        socket1.close();
        Socket socket2 = new Socket();
        socket2.connect(socketAddress);
        socket2.close();
        InetSocketAddress inetSocketAddress1 = (InetSocketAddress)socketAddress;
        System.out.println("服务器域名:" + inetSocketAddress1.getAddress().getHostName());
        System.out.println("服务器IP:" + inetSocketAddress1.getAddress().getHostAddress());
        System.out.println("服务器端口:" + inetSocketAddress1.getPort());
        InetSocketAddress inetSocketAddress2 = (InetSocketAddress) socket2.getLocalSocketAddress();
        System.out.println("本地IP:" + inetSocketAddress2.getAddress().getLocalHost().getHostAddress());
        System.out.println("本地端口:" + inetSocketAddress2.getPort());
        long i = 0l;
        System.out.println("" + i);
    }
}
