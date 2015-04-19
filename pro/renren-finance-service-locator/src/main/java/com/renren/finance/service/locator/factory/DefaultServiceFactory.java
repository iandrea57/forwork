/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import com.renren.finance.service.locator.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午4:47
 */
public class DefaultServiceFactory implements IServiceFactory {

    private static final long DEFAULT_TIME_OUT = 300L;

    private ConcurrentMap<Class<?>, Object> serviceCache = new ConcurrentHashMap<Class<?>, Object>();


    @Override
    public <T> T getService(Class<T> serviceInterface) {
        return getService(serviceInterface, DEFAULT_TIME_OUT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> serviceInterface, long timeout) {
        Object serviceInstance = serviceCache.get(serviceInterface);

        if (serviceInstance != null) {
            return (T) serviceInstance;
        }

        try {
            ClassDefinition serviceDefinition = new ClassDefinition(serviceInterface);
            InvocationHandler handler = createInvocationHandler(serviceDefinition, timeout);
            T proxy = (T) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[] {serviceInterface}, handler);
            Object temp = serviceCache.putIfAbsent(serviceInterface, proxy);
            if (temp != null) return (T) temp;
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationHandler createInvocationHandler(ClassDefinition serviceDefinition, long timeout) {
        return new ServiceInvocationHandler(serviceDefinition, timeout);
    }
}
