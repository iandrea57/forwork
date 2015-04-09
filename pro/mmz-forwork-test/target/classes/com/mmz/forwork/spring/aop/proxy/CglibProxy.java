/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.forwork.spring.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-9 下午6:36
 */
public class CglibProxy {

    public static class CglibProxyFactory implements MethodInterceptor {

        private Object delegate;

        public Object bind(Object delegate) {
            this.delegate = delegate;

            Enhancer hancer = new Enhancer();
            // 设置父类
            hancer.setSuperclass(delegate.getClass());
            // 回调
            hancer.setCallback(this);
            return hancer.create();
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                throws Throwable {
            System.out.println("start method: " + method.getName());
            Object result = null;
            try {
                result = proxy.invoke(delegate, args);
            } catch (Exception e) {}
            System.out.println("end method: " + method.getName());
            System.out.println();
            return result;
        }
    }

    public static void main(String[] args) {
        OfferImpl offer = (OfferImpl) (new CglibProxyFactory().bind(new OfferImpl()));
        offer.postOffer();
        offer.modifyOffer();
    }
}
