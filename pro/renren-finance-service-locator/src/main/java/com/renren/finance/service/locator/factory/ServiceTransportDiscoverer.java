/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import com.renren.finance.service.locator.model.Node;
import com.renren.finance.service.locator.discoverer.DefaultNodeDiscoverer;
import com.renren.finance.service.locator.discoverer.INodeDiscoverer;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午6:00
 */
public class ServiceTransportDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTransportDiscoverer.class);

    private static ServiceTransportDiscoverer instance = new ServiceTransportDiscoverer();

    private TTransportProvider connectionProvider = new TTransportProvider();

    private INodeDiscoverer nodeDiscoverer = DefaultNodeDiscoverer.getInstance();

    public ServiceTransport get(String serviceId, int timeout) throws Exception {
        if (!isValidServiceId(serviceId)) {
            throw new IllegalArgumentException("serviceId invalid!");
        }

        Node node = null;
        int retry = 0;
        TTransport transport = null;
        ServiceTransport serviceTransport = null;
        while (true) {
            node = nodeDiscoverer.getNode(serviceId);
            if (node == null) {
                logger.error("No endpoint available : " + serviceId);
                return null;
            }
            serviceTransport = new ServiceTransport();
            serviceTransport.setNode(node);
            serviceTransport.setTimeout(timeout);
            try {
                transport = connectionProvider.getConnection(node, timeout);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (logger.isDebugEnabled()) {
            logger.debug("get service ok : " + serviceId);
        }

        serviceTransport.setTransport(transport);
        return serviceTransport;
    }

    private boolean isValidServiceId(String serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            return false;
        }
        return true;
    }

    public void returnConn(ServiceTransport serviceTransport) throws Exception {
        connectionProvider.returnConnection(serviceTransport.getNode(),
                serviceTransport.getTimeout(), serviceTransport.getTransport());
    }

    public void serviceException(String serviceId, Throwable e, ServiceTransport serviceTransport) {
    }

    public static ServiceTransportDiscoverer getInstance() {
        return instance;
    }
}
