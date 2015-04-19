/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.test;

import com.mmz.frameworkuse.thrift.IInfoService;

import java.lang.reflect.Type;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午4:54
 */
public class Test {

    public static void main(String[] args) {
        Class<?> class1 = IInfoService.class;
        Type type2 = class1.getGenericInterfaces()[0];


        print(class1.getPackage().getName());
        print(class1.getSimpleName());
        print(type2.toString());

        Class<?> class2 = (Class<?>)type2;
        print(class2.getPackage());
        print(class2.getSimpleName());
        print(class2.getName());

        print(resolveClientClassName(class1));


        // com.renren.xoa2.client.ServiceFactory;
    }
    private static String resolveClientClassName(Class<?> serviceInterface) {
        Class<?> ifaceClass = (Class<?>)serviceInterface.getGenericInterfaces()[0];
        String ifaceClassName = ifaceClass.getName();
        return ifaceClassName.substring(0, ifaceClassName.lastIndexOf("$")) + "$Client";
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }
}
