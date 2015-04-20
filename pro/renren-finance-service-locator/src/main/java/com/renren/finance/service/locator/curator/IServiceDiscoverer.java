package com.renren.finance.service.locator.curator;

import com.renren.finance.service.locator.factory.Node;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-20 上午11:44
 */
public interface IServiceDiscoverer {

    public Node getNode(String serviceId);
}
