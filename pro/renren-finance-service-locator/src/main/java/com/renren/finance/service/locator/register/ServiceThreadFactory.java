/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-5-4 下午4:20
 */
public class ServiceThreadFactory implements ThreadFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServiceThreadFactory.class);

    /**
     * 错误处理: 暂时仅日志打印
     */
    private static class ServiceUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private static final Logger logger = LoggerFactory.getLogger(ServiceUncaughtExceptionHandler.class);
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("Service thread exception/error in : " + t.getName(), e);
        }
    }

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public ServiceThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";

        Thread.setDefaultUncaughtExceptionHandler(null);
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);

        t.setUncaughtExceptionHandler(new ServiceUncaughtExceptionHandler());
        logger.debug("ServiceThreadFactory.newThread: {}", t.getName());
        return t;
    }
}
