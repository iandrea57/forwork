/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午6:00
 */
public class CommonServiceRouter implements ServiceRouter {

    private static CommonServiceRouter instance = new CommonServiceRouter();

    private TTransportProvider connectionProvider = new TTransportProvider();

    @Override
    public FinanceTransport routeService(String serviceId, long timeout) throws Exception {
        if (!isValidServiceId(serviceId)) {
            throw new IllegalArgumentException("serviceId invalid!");
        }

        TTransport transport = connectionProvider.getConnection();
    }

    private boolean isValidServiceId(String serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void returnConn(FinanceTransport financeTransport) throws Exception {
        connectionProvider.returnConnection(financeTransport);
    }

    @Override
    public void serviceException(String serviceId, Throwable e, FinanceTransport financeTransport) {
    }

    @Override
    public void setTimeout(long timeout) {
    }
}
