/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-14 下午5:20
 */
public class ServiceFactory {

    private static final int DEFAULT_TIME_OUT = 300;

    private static IServiceFactory factory = new DefaultServiceFactory();

    public static <T> T getService(Class<T> serviceInterface) {
        return getService(serviceInterface, DEFAULT_TIME_OUT);
    }

    public static <T> T getService(Class<T> serviceInterface, int timeout) {
        return factory.getService(serviceInterface, timeout);
    }
}
