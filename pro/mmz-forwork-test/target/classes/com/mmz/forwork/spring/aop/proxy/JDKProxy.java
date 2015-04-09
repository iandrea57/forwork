/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.forwork.spring.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-9 下午6:28
 */
public class JDKProxy {

    public static class ProxyFactory implements InvocationHandler {

        private Object delegate;

        public Object bind(Object delegate) {
            this.delegate = delegate;
            return Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
                    delegate.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("start method: " + method.getName());
            Object result = null;
            try {
                result = method.invoke(delegate, args);
            } catch (Exception e) {}
            System.out.println("end method: " + method.getName());
            System.out.println();
            return result;
        }
    }

    public static void main(String[] args) {
        IOffer offer = (IOffer) (new ProxyFactory().bind(new OfferImpl()));
        offer.postOffer();
        offer.modifyOffer();
    }

}
