/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.registrar;

import com.renren.finance.service.locator.register.RegisterInfo;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-28 下午5:47
 */
public interface IServiceRegistrar {

    public void register(String serviceId, RegisterInfo info) throws Exception;

    public void unregister(String serviceId, RegisterInfo info) throws Exception;

}
