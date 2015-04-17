/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午3:59
 */
public class InstanceDetail {

    private String id;

    private String ip;

    private int port;

    private String interfaceName;

    public InstanceDetail(String id, String ip, int port, String interfaceName) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.interfaceName = interfaceName;
    }

    public InstanceDetail() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "InstanceDetail{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", interfaceName='" + interfaceName + '\'' +
                "} " + super.toString();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

}
