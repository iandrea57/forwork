package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-14 下午5:19
 */
public interface IServiceFactory {

    public <T> T getService(Class<T> serviceInterface);

    public <T> T getService(Class<T> serviceInterface, int timeout);

}
