/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import org.apache.thrift.TBase;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午6:18
 */
public class ServiceInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInvocationHandler.class);
    private static final int MAX_RETRY = 1;
    private ClassDefinition serviceDefinition;
    private ServiceRouter serviceRouter;
    private int timeout;
    private ConcurrentMap<Method, MethodDefinition> methodCache = new ConcurrentHashMap<Method, MethodDefinition>();

    public ServiceInvocationHandler(ClassDefinition serviceDefinition, int timeout) {
        this(CommonServiceRouter.getInstance(), serviceDefinition, timeout);
    }

    public ServiceInvocationHandler(ServiceRouter serviceRouter, ClassDefinition serviceDefinition, int timeout) {
        if (serviceRouter == null || serviceDefinition == null) {
            throw new NullPointerException();
        }
        this.serviceRouter = serviceRouter;
        this.serviceDefinition = serviceDefinition;
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceId = serviceDefinition.getServiceId();

        FinanceTransport financeTransport = null;
        int retry = 0;
        try {
            while (true) {
                financeTransport = serviceRouter.routeService(serviceId, timeout);
                if (financeTransport != null) {
                    break;
                }
                if (++retry >= MAX_RETRY) {
                    logger.error(serviceId + " get financeTransport failed.");
                    return null;
                }
            }
            TProtocol protocol = new TBinaryProtocol(financeTransport.getTransport());
            Object client = serviceDefinition.getServiceClientConstructor().newInstance(protocol);
            Object result = getRealMethod(method).getMethod().invoke(client, args);
            serviceRouter.returnConn(financeTransport);
            return result;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof TBase) {
                serviceRouter.returnConn(financeTransport);
                logger.error("Thrift service return exception");
            }
            throw cause;
        } catch (Exception e) {
            serviceRouter.serviceException(serviceId, e, financeTransport);
            throw new Exception("failed to route " + serviceId, e);
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
