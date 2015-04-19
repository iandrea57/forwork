/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午6:18
 */
public class ServiceInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInvocationHandler.class);
    private ClassDefinition serviceDefinition;
    private long timeout;
    private ConcurrentMap<Method, MethodDefinition> methodCache = new ConcurrentHashMap<Method, MethodDefinition>();

    public ServiceInvocationHandler(ClassDefinition serviceDefinition, long timeout) {
        if (serviceDefinition == null) {
            throw new NullPointerException();
        }
        this.serviceDefinition = serviceDefinition;
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodDefinition methodDefinition = null;
        methodDefinition = getRealMethod(method);

        String serviceId = serviceDefinition.getServiceId();

        ServiceRouter serviceRouter = new CommonServiceRouter();
        FinanceTransport transport = null;
        try {
            transport = serviceRouter.routeService(serviceId, timeout);
        } catch (Exception e) {
            throw new Exception("failed to route " + serviceId, e);
        }
        if (transport == null) {
            throw new Exception("No transport avalible for " + serviceId);
        }

        Object result = null;
        try {
            TProtocol protocol = new TBinaryProtocol(transport.getTransport());
            Object client = serviceDefinition.getServiceClientConstructor().newInstance(protocol);
            result = getRealMethod(method).getMethod().invoke(client, args);
            return result;
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            serviceRouter.returnConn(transport);
        }

    }

    private MethodDefinition getRealMethod(Method method) throws NoSuchMethodException {
        MethodDefinition methodDefinition = methodCache.get(method);
        if (methodDefinition != null) {
            return methodDefinition;
        }
        Method realMethod = serviceDefinition.getServiceClientClass().getMethod(method.getName(),
                method.getParameterTypes());
        methodDefinition = new MethodDefinition(realMethod);
        methodCache.put(method, methodDefinition);
        return methodDefinition;
    }
}
