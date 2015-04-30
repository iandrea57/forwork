/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import com.renren.finance.service.locator.annotation.FinanceService;

import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-28 下午5:37
 */
public class ServerDefinition {

    private String serviceId;

    private Class<?> serviceProcessorClass;

    private Constructor<?> serviceProcessorConstructor;

    public ServerDefinition(Class<?> serviceInterface) throws ClassNotFoundException, NoSuchMethodException {
        this.serviceProcessorClass = Class.forName(resolveProcessorClassName(serviceInterface));
        this.serviceId = resolveServiceId(serviceInterface);
        this.serviceProcessorConstructor = serviceProcessorClass.getConstructor(getIfaceClass(serviceInterface));
    }

    private String resolveServiceId(Class<?> serviceInterface) {
        FinanceService financeService = serviceInterface.getAnnotation(FinanceService.class);
        return financeService != null ? financeService.value().trim() : "";
    }

    private String resolveProcessorClassName(Class<?> serviceInterface) {
        Class<?> ifaceClass = getIfaceClass(serviceInterface);
        String processorClassName = null;
        if (ifaceClass != null) {
            String ifaceClassName = ifaceClass.getName();
            int indexOf$ = ifaceClassName.lastIndexOf("$");
            if (indexOf$ > -1) {
                processorClassName = ifaceClassName.substring(0, indexOf$) + "$Processor";
            }
        }
        return processorClassName;
    }

    private Class<?> getIfaceClass(Class<?> serviceInterface) {
        return (Class<?>)serviceInterface.getGenericInterfaces()[0];
    }

    public String getServiceId() {
        return serviceId;
    }

    public Class<?> getServiceProcessorClass() {
        return serviceProcessorClass;
    }

    public Constructor<?> getServiceProcessorConstructor() {
        return serviceProcessorConstructor;
    }
}
