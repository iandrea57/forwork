/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.frameworkuse.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-4-29 下午5:42
 */
public class ZkClient {

    private static final int DEFAULT_SESSION_TIMEOUT = 10000;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    public void deleteNodeRecursive(String connectString, String path) {
        try {
            System.out.println("--- connecting: " + connectString + " ---");
            ZooKeeper zooKeeper = new ZooKeeper(connectString, DEFAULT_SESSION_TIMEOUT, new ConnectWatcher(this), true);
            connectedSignal.await();
            connectedSignal = new CountDownLatch(1);

            deleteNodeRecursive(zooKeeper, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteNodeRecursive(ZooKeeper zooKeeper, String path) throws Exception {
        System.out.println("--- getting Children: " + path + " ---");
        List<String> children = zooKeeper.getChildren(path, new GetChildrenWatcher());
        for (String child : children) {
            child = path + "/" + child;
            System.out.println(child);
            deleteNodeRecursive(zooKeeper, child);
        }
        System.out.println("--- delete Node: " + path + " ---");
        zooKeeper.delete(path, -1);
    }


    public static class ConnectWatcher implements Watcher {

        private ZkClient client;

        public ConnectWatcher(ZkClient client) {
            this.client = client;
        }

        @Override
        public void process(WatchedEvent event) {
            System.out.println("----------- connect watcher callback -------------");
            System.out.println(event);
            if (event.getState() == Event.KeeperState.SyncConnected) {
                client.connectedSignal.countDown();
            }
        }
    }

    public static class GetChildrenWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            System.out.println("----------- getChildren watcher callback -------------");
            System.out.println(event);
        }
    }

    public void getChildren(String connectString, String path) {
        try {
            System.out.println("--- connecting: " + connectString + " ---");
            ZooKeeper zooKeeper = new ZooKeeper(connectString, DEFAULT_SESSION_TIMEOUT, new ConnectWatcher(this), true);

            Stat st = new Stat();
            List<String> stat = zooKeeper.getChildren(path, null, st);
            for (String s : stat) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ZkClient client = new ZkClient();
//        client.deleteNodeRecursive("10.4.30.143:2181,10.4.30.144:2181,10.4.30.144:2183", "/finance.cache");
//        client.deleteNodeRecursive("10.4.30.143:2181,10.4.30.144:2181,10.4.30.144:2183", "/redis-ha");
//        client.deleteNodeRecursive("10.3.24.123:12181", "/test");
        client.getChildren("10.3.24.123:12181", "/");
    }
}
