/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.frameworkuse.thrift;

import com.mmz.service.api.InfoService;
import com.renren.finance.service.locator.annotation.Locator;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-10 下午12:09
 */
@Locator("infoservice")
public interface IInfoService extends InfoService.Iface {
}
