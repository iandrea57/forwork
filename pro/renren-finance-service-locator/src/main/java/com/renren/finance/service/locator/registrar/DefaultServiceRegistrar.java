/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.finance.service.locator.registrar;

import com.renren.finance.service.locator.conf.LocatorConf;
import com.renren.finance.service.locator.curator.NodeInstanceDetail;
import com.renren.finance.service.locator.register.RegisterInfo;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-7 下午4:01
 */
public class DefaultServiceRegistrar implements IServiceRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistrar.class);

    private ServiceDiscovery<NodeInstanceDetail> serviceDiscovery;

    private static class RegistrarHolder {

        private static final DefaultServiceRegistrar instance = getRegistrar();

        private static DefaultServiceRegistrar getRegistrar() {
            LocatorConf conf = LocatorConf.instance();

            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(conf.getCluster())
                    .retryPolicy(retryPolicy)
                    .build();
            client.start();

            DefaultServiceRegistrar discoverer = null;
            try {
                discoverer = new DefaultServiceRegistrar(client, conf.getBasePath());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("DefaultServiceDiscoverer.DiscovererHolder.getDiscoverer", e);
            }
            return discoverer;
        }
    }

    public DefaultServiceRegistrar(CuratorFramework client, String basePath) throws Exception {
        serviceDiscovery = ServiceDiscoveryBuilder.builder(NodeInstanceDetail.class)
                .client(client)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public static IServiceRegistrar getInstance() {
        return RegistrarHolder.instance;
    }

    @Override
    public void register(String serviceId, RegisterInfo info) throws Exception {
        String host = info.getNode().getHost();
        int port = info.getNode().getPort();
        ServiceInstance<NodeInstanceDetail> instance = ServiceInstance.<NodeInstanceDetail>builder()
                .name(serviceId)
                .port(port)
                .address(host)
                .payload(new NodeInstanceDetail(UUID.randomUUID().toString(),host,port,info.getInterfaceName(), info.getPhones()))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        serviceDiscovery.registerService(instance);
    }

    @Override
    public void unregister(String serviceId, RegisterInfo info) throws Exception {
        String host = info.getNode().getHost();
        int port = info.getNode().getPort();
        ServiceInstance<NodeInstanceDetail> instance = ServiceInstance.<NodeInstanceDetail>builder()
                .name(serviceId)
                .port(port)
                .address(host)
                .payload(new NodeInstanceDetail(UUID.randomUUID().toString(),host,port,info.getInterfaceName(), info.getPhones()))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        serviceDiscovery.unregisterService(instance);
    }
}
