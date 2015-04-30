/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import com.renren.finance.service.locator.discoverer.DefaultServiceDiscoverer;
import com.renren.finance.service.locator.discoverer.IServiceDiscoverer;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午6:00
 */
public class CommonServiceRouter implements ServiceRouter {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceRouter.class);

    private static CommonServiceRouter instance = new CommonServiceRouter();

    private TTransportProvider connectionProvider = new TTransportProvider();

    private IServiceDiscoverer serviceDiscoverer = DefaultServiceDiscoverer.getInstance();

    @Override
    public FinanceTransport routeService(String serviceId, int timeout) throws Exception {
        if (!isValidServiceId(serviceId)) {
            throw new IllegalArgumentException("serviceId invalid!");
        }

        Node node = null;
        int retry = 0;
        TTransport transport = null;
        FinanceTransport financeTransport = null;
        while (true) {
            node = serviceDiscoverer.getNode(serviceId);
            if (node == null) {
                logger.error("No endpoint available : " + serviceId);
                return null;
            }
            financeTransport = new FinanceTransport();
            financeTransport.setNode(node);
            financeTransport.setTimeout(timeout);
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

        financeTransport.setTransport(transport);
        return financeTransport;
    }

    private boolean isValidServiceId(String serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void returnConn(FinanceTransport financeTransport) throws Exception {
        connectionProvider.returnConnection(financeTransport.getNode(),
                financeTransport.getTimeout(), financeTransport.getTransport());
    }

    @Override
    public void serviceException(String serviceId, Throwable e, FinanceTransport financeTransport) {
    }

    public static CommonServiceRouter getInstance() {
        return instance;
    }
}
