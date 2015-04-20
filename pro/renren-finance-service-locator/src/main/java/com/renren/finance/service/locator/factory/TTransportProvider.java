package com.renren.finance.service.locator.factory;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

/**
 * Created by dra on 15-4-19.
 */
public class TTransportProvider {

    private Logger logger = LoggerFactory.getLogger(TTransportProvider.class);

    private static int maxTotle = 200;

    private static int maxIdle = 40;

    private static int minIdle = 10;

    private static long maxWaitMillis = 10000;

    private static boolean testOnBorrow;
    private static boolean testOnReturn;
    private static boolean testOnCreate;
    private static boolean testWhileIdle;

    public static void setPoolParam(int maxTotle, int maxIdle, int minIdle, long maxWaitMillis) {
        TTransportProvider.maxTotle = maxTotle;
        TTransportProvider.maxIdle = maxIdle;
        TTransportProvider.minIdle = minIdle;
        TTransportProvider.maxWaitMillis = maxWaitMillis;
    }

    private static class Creator {

        private static final KeyedObjectPool<String, TTransport> instance = createPool();

        private static KeyedObjectPool<String, TTransport> createPool() {
            ThriftPoolableObjectFactory thriftPoolableObjectFactory = new ThriftPoolableObjectFactory();
            GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
            poolConfig.setTestWhileIdle(testOnBorrow);
            poolConfig.setTestOnCreate(testOnCreate);
            poolConfig.setTestOnBorrow(testOnBorrow);
            poolConfig.setTestOnReturn(testOnReturn);
            poolConfig.setMaxTotal(-1);
            poolConfig.setMaxTotalPerKey(maxTotle);
            poolConfig.setMaxIdlePerKey(maxIdle);
            poolConfig.setMinIdlePerKey(minIdle);
            poolConfig.setMaxWaitMillis(maxWaitMillis);
            poolConfig.setBlockWhenExhausted(true);

            GenericKeyedObjectPool<String, TTransport> keyedObjectPool = new GenericKeyedObjectPool<String, TTransport>(thriftPoolableObjectFactory, poolConfig);
            return keyedObjectPool;
        }

    }

    private static KeyedObjectPool<String, TTransport> getKeyedObjectPool() {
        return Creator.instance;
    }

    public static String getConnStatus() {
        StringBuilder message = new StringBuilder();

        KeyedObjectPool<String, TTransport> keyedObjectPool = getKeyedObjectPool();
        message.append("Status of connection is:" + "\n pool using size: "
                + keyedObjectPool.getNumActive() + "\n pool idle size:" + keyedObjectPool.getNumIdle() + '\n');
        return message.toString();
    }

    public TTransport getConnection(Node node, long conTimeOut) throws Exception {
        KeyedObjectPool<String, TTransport> keyedObjectPool = getKeyedObjectPool();
        String key = ThriftPoolableObjectFactory.getKey(node.getHost(), node.getPort(), conTimeOut);
        TTransport transport = null;
        try {
            transport = keyedObjectPool.borrowObject(key);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("pool-stat: alloc "+ transport + ",active=" + keyedObjectPool.getNumActive()
                    + ",idle=" + keyedObjectPool.getNumIdle());
        }
        return transport;
    }

    public void returnConnection(Node node, long conTimeOut, TTransport transport) {
        String key = ThriftPoolableObjectFactory.getKey(node.getHost(), node.getPort(), conTimeOut);
        if (transport != null) {
            try {
                getKeyedObjectPool().returnObject(key, transport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void invalidConnection(Node node, long conTimeOut, TTransport transport) {
        String key = ThriftPoolableObjectFactory.getKey(node.getHost(), node.getPort(), conTimeOut);
        if (transport != null) {
            try {
                getKeyedObjectPool().invalidateObject(key, transport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearConnection(Node node, long conTimeOut) {
        String key = ThriftPoolableObjectFactory.getKey(node.getHost(), node.getPort(), conTimeOut);
        try {
            getKeyedObjectPool().clear(key);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public static void setTestOnBorrow(boolean testOnBorrow) {
        TTransportProvider.testOnBorrow = testOnBorrow;
    }

    public static boolean isTestOnReturn() {
        return testOnReturn;
    }

    public static void setTestOnReturn(boolean testOnReturn) {
        TTransportProvider.testOnReturn = testOnReturn;
    }

    public static boolean isTestOnCreate() {
        return testOnCreate;
    }

    public static void setTestOnCreate(boolean testOnCreate) {
        TTransportProvider.testOnCreate = testOnCreate;
    }

    public static boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public static void setTestWhileIdle(boolean testWhileIdle) {
        TTransportProvider.testWhileIdle = testWhileIdle;
    }
}

