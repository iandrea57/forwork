/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.curator;

import com.renren.finance.service.locator.factory.Node;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-20 下午2:40
 */
public class ServiceNode implements Node {

    private String host;
    private int port;

    private boolean disabled;
    private boolean healthy;


    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isHealthy() {
        return healthy;
    }

    @Override
    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public int compareTo(Node o) {
        return host.compareTo(o.getHost());
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
