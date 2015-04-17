/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午5:57
 */
public interface Node extends Comparable<Node> {

    public String getHost();
    public int getPort();

    public boolean isDisabled();
    public void setDisabled(boolean disabled);

    public boolean isHealthy();
    public void setHealthy(boolean healthy);
}
