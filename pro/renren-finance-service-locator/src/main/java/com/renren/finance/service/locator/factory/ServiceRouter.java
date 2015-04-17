package com.renren.finance.service.locator.factory;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-17 下午5:54
 */
public interface ServiceRouter {

    public FinanceTransport routeService(String serviceId, String shardBy, int timeout) throws Exception;

    public void returnConn(FinanceTransport financeTransport) throws Exception;

    public void serviceException(String serviceId, Throwable e, FinanceTransport financeTransport);

    public void setTimeout(long timeout);
}
