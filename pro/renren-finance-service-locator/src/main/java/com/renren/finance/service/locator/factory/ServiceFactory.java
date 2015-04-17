/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-14 下午5:20
 */
public class ServiceFactory implements IServiceFactory {

    private static final long DEFAULT_TIME_OUT = 300L;

    @Override
    public <T> T getService(Class<T> serviceInterface) {
        return getService(serviceInterface, DEFAULT_TIME_OUT);
    }

    @Override
    public <T> T getService(Class<T> serviceInterface, long timeout) {
        return null;
    }
}
