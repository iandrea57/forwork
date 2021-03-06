/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import com.renren.finance.service.locator.model.Node;
import org.apache.thrift.transport.TTransport;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午5:56
 */
public class ServiceTransport {

    private TTransport transport;

    private Node node;

    private int timeout;

    private boolean disabled = false;

    public TTransport getTransport() {
        return transport;
    }

    public void setTransport(TTransport transport) {
        this.transport = transport;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
