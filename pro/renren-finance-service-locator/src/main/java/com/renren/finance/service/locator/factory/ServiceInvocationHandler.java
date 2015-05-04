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
    private ClientClassDefinition serviceDefinition;
    private ServiceTransportDiscoverer serviceTransportDiscoverer;
    private int timeout;
    private ConcurrentMap<Method, Method> methodCache = new ConcurrentHashMap<Method, Method>();

    public ServiceInvocationHandler(ClientClassDefinition serviceDefinition, int timeout) {
        this(ServiceTransportDiscoverer.getInstance(), serviceDefinition, timeout);
    }

    public ServiceInvocationHandler(ServiceTransportDiscoverer serviceTransportDiscoverer, ClientClassDefinition serviceDefinition, int timeout) {
        if (serviceTransportDiscoverer == null || serviceDefinition == null) {
            throw new NullPointerException();
        }
        this.serviceTransportDiscoverer = serviceTransportDiscoverer;
        this.serviceDefinition = serviceDefinition;
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceId = serviceDefinition.getServiceId();

        ServiceTransport serviceTransport = null;
        int retry = 0;
        try {
            while (true) {
                serviceTransport = serviceTransportDiscoverer.get(serviceId, timeout);
                if (serviceTransport != null) {
                    break;
                }
                if (++retry >= MAX_RETRY) {
                    logger.error(serviceId + " get financeTransport failed.");
                    return null;
                }
            }
            TProtocol protocol = new TBinaryProtocol(serviceTransport.getTransport());
            Object client = serviceDefinition.getServiceClientConstructor().newInstance(protocol);
            Object result = getRealMethod(method).invoke(client, args);
            serviceTransportDiscoverer.returnConn(serviceTransport);
            return result;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof TBase) {
                serviceTransportDiscoverer.returnConn(serviceTransport);
                logger.error("Thrift service return exception");
            }
            throw cause;
        } catch (Exception e) {
            serviceTransportDiscoverer.serviceException(serviceId, e, serviceTransport);
            throw new Exception("failed to route " + serviceId, e);
        }
    }

    private Method getRealMethod(Method method) throws NoSuchMethodException {
        Method realMethod = methodCache.get(method);
        if (realMethod != null) {
            return realMethod;
        }
        realMethod = serviceDefinition.getServiceClientClass().getMethod(method.getName(),
                method.getParameterTypes());
        methodCache.put(method, realMethod);
        return realMethod;
    }
}
