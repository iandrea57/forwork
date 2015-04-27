package com.renren.finance.service.locator.factory;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dra on 15-4-19.
 */
public class ThriftPoolableObjectFactory extends BaseKeyedPooledObjectFactory<String, TTransport> {

    private Logger logger = LoggerFactory.getLogger(ThriftPoolableObjectFactory.class);

    public static String getKey(String serviceIp, int servicePort, long timeout) {
        return String.format("%s:%d:%d", serviceIp, servicePort, timeout);
    }

    private static String getIp(String key) {
        return key.split(":")[0];
    }

    private static int getPort(String key) {
        return Integer.parseInt(key.split(":")[1]);
    }

    private static int getTimeout(String key) {
        return Integer.parseInt(key.split(":")[2]);
    }

    @Override
    public TTransport create(String key) throws Exception {
        try {
            TSocket socket = new TSocket(getIp(key), getPort(key));
            socket.getSocket().setKeepAlive(true);
            socket.getSocket().setTcpNoDelay(true);
            socket.getSocket().setSoLinger(false, 0);
            socket.getSocket().setSoTimeout(getTimeout(key));
//            TTransport transport = new TFramedTransport(socket);
            TTransport transport = socket;
            transport.open();
            if (logger.isDebugEnabled()) {
                logger.debug("client pool make object success.");
            }
            return transport;
        } catch (Exception e) {
            logger.warn("client pool make object error.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public PooledObject<TTransport> wrap(TTransport transport) {
        return new DefaultPooledObject<TTransport>(transport);
    }
}
