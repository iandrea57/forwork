#### redis安装

* tar -xzvf redis-2.6.16.tar.gz
* cd redis-2.6.16
* make PREFIX=/data/hailong.peng/redis/redis-2.6.16 install

#### 命令
    
    ./redis-server ../redis.conf
    ./redis-cli -h 10.4.35.94 -p 63797

#### redis.conf

    pidfile /var/run/redis_63791_1.pid
    port 63791
    logfile redis_63791_1.log
    dbfilename dump_63791_1.rdb
    dir /data/zoo/redis-cluster.finance.cache/redis-2.6.16/
    slave-read-only no
    maxmemory 5368709120
    maxmemory-policy allkeys-lru
    appendfilename appendonly_63791_1.aof
    no-appendfsync-on-rewrite yes
    auto-aof-rewrite-percentage 1001
    slowlog-max-len 1024
    

#### redis配合zookeeper集群搭建
/data/zoo/jdk/bin/java -Drediscluster.log.dir=/data/zoo/redis-cluster.finance.cache/agent/logs -Drediscluster.root.logger=INFO,NORMAL -Drediscluster.log.file=agent_10.4.35.95_63794_4_54794.log -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy -XX:-UseGCOverheadLimit -Xms128M -Xmx256M -cp /data/zoo/redis-cluster.finance.cache/agent/RedisCluster-2.1.0.0-SNAPSHOT-jar-with-dependencies-20121112.jar com.renren.cluster.agent.redis.RedisAgent finance.cache 4 webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181 10.4.35.95 63794 54794

    /data/zoo/jdk/bin/java
    -Drediscluster.log.dir=/data/zoo/redis-cluster.finance.cache/agent/logs
    -Drediscluster.root.logger=INFO,NORMAL
    -Drediscluster.log.file=agent_10.4.35.95_63794_4_54794.log
    -XX:+UseParallelGC
    -XX:+UseParallelOldGC
    -XX:+UseAdaptiveSizePolicy
    -XX:-UseGCOverheadLimit
    -Xms128M
    -Xmx256M
    -cp
    /data/zoo/redis-cluster.finance.cache/agent/RedisCluster-2.1.0.0-SNAPSHOT-jar-with-dependencies-20121112.jar

    com.renren.cluster.agent.redis.RedisAgent

    finance.cache
    4
    webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181
    10.4.35.95
    63794
    54794
   

##### zk数据

    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 21] ls /redis-ha/finance.cache/0/10.4.35.94:63790
    [93408616136465128-0000000000, 309365719473195892-0000000001]
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 22] get /redis-ha/finance.cache/0/10.4.35.94:63790/93408616136465128-0000000000
    10.4.35.94:53790
    cZxid = 0x3252f78554
    ctime = Mon Mar 23 17:12:47 CST 2015
    mZxid = 0x3252f78554
    mtime = Mon Mar 23 17:12:47 CST 2015
    pZxid = 0x3252f78554
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x14bdaa437c65ae8
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 23] get /redis-ha/finance.cache/0/10.4.35.94:63790/309365719473195892-0000000001
    10.4.35.95:54790
    cZxid = 0x3252f78599
    ctime = Mon Mar 23 17:12:47 CST 2015
    mZxid = 0x3252f78599
    mtime = Mon Mar 23 17:12:47 CST 2015
    pZxid = 0x3252f78599
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x44b167b96e20b74
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 24] ls /redis-ha/finance.cache/0/10.4.35.94:63794
    Node does not exist: /redis-ha/finance.cache/0/10.4.35.94:63794
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 25] ls /redis-ha/finance.cache/4/10.4.35.94:63794
    [381423313516962814-0000000000, 309365719473195894-0000000001]
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 26] get /redis-ha/finance.cache/4/10.4.35.94:63794/381423313516962814-0000000000
    10.4.35.94:53794
    cZxid = 0x3252f787e4
    ctime = Mon Mar 23 17:12:50 CST 2015
    mZxid = 0x3252f787e4
    mtime = Mon Mar 23 17:12:50 CST 2015
    pZxid = 0x3252f787e4
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x54b167b973b23fe
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 27] get /redis-ha/finance.cache/4/10.4.35.94:63794/309365719473195894-0000000001
    10.4.35.95:54794
    cZxid = 0x3252f78831
    ctime = Mon Mar 23 17:12:50 CST 2015
    mZxid = 0x3252f78831
    mtime = Mon Mar 23 17:12:50 CST 2015
    pZxid = 0x3252f78831
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x44b167b96e20b76
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 28] ls /redis-ha/finance.cache
    [15, 13, 14, 11, 12, 3, 2, 1, 10, 0, 7, 6, 5, 4, 9, 8]
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 29] ls /redis-ha/finance.cache/4
    [10.4.35.94:63794, 10.4.35.95:63794]
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 30] ls /finance.cache/4
    [node-237308125435269078-0000000001, node-381423313516962814-0000000000]
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 31] get /finance.cache/4/node-237308125435269078-0000000001
    10.4.35.95:63794
    cZxid = 0x3252f79a08
    ctime = Mon Mar 23 17:13:06 CST 2015
    mZxid = 0x3252f79a08
    mtime = Mon Mar 23 17:13:06 CST 2015
    pZxid = 0x3252f79a08
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x0
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 32] get /finance.cache/4/node-381423313516962814-0000000000
    10.4.35.94:63794
    cZxid = 0x3252f787de
    ctime = Mon Mar 23 17:12:50 CST 2015
    mZxid = 0x3252f787de
    mtime = Mon Mar 23 17:12:50 CST 2015
    pZxid = 0x3252f787de
    cversion = 0
    dataVersion = 0
    aclVersion = 0
    ephemeralOwner = 0x0
    dataLength = 16
    numChildren = 0
    [zk: webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181(CONNECTED) 33] 