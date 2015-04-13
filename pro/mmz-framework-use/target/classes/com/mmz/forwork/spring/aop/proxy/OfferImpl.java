/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.forwork.spring.aop.proxy;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-9 下午6:33
 */
public class OfferImpl implements IOffer {
    @Override
    public void postOffer() {
        System.out.println("post offer");
    }

    @Override
    public void modifyOffer() {
        System.out.println("modify offer");
    }

}
