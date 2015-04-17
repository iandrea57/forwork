/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

import com.renren.finance.service.locator.annotation.FinanceService;
import org.apache.thrift.protocol.TProtocol;

import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午4:50
 */
public class ClassDefinition {

    private String serviceId;

    private Class<?> serviceClientClass;

    private Constructor<?> serviceClientConstructor;

    public ClassDefinition(Class<?> serviceInterface) throws ClassNotFoundException, NoSuchMethodException {
        this.serviceClientClass = Class.forName(resolveClientClassName(serviceInterface));
        this.serviceId = resolveServiceId(serviceInterface);
        this.serviceClientConstructor = serviceClientClass.getConstructor(TProtocol.class);
    }

    private String resolveServiceId(Class<?> serviceInterface) {
        FinanceService financeService = serviceInterface.getAnnotation(FinanceService.class);
        return financeService != null ? financeService.value().trim() : "";
    }

    private String resolveClientClassName(Class<?> serviceInterface) {
        Class<?> ifaceClass = (Class<?>)serviceInterface.getGenericInterfaces()[0];
        String clientClassName = null;
        if (ifaceClass != null) {
            String ifaceClassName = ifaceClass.getName();
            int indexOf$ = ifaceClassName.lastIndexOf("$");
            if (indexOf$ > -1) {
                clientClassName = ifaceClassName.substring(0, indexOf$) + "$Client";
            }
        }
        return clientClassName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Class<?> getServiceClientClass() {
        return serviceClientClass;
    }

    public Constructor<?> getServiceClientConstructor() {
        return serviceClientConstructor;
    }
}
