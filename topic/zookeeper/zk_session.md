#### session基础
##### 什么是session
zookeeper中session意味着一个物理连接，客户端connect成功之后，会发送一个connect型请求，此时就会有session 产生（下面会具体讲）

##### sessionid是如何产生的
在SessionTrackerImpl实例化的时候就会调用下面的函数【详见SessionTrackerImpl.initializeNextSession】

    public static long initializeNextSession(long id) {
       long nextSid = 0;
       nextSid = (System.currentTimeMillis() << 24) >> 8;
       nextSid =  nextSid | (id <<56);
       return nextSid;
    }

产生的值会存入nextSessionId属性，以后一旦有新的连接（session）产生，就会nextSessionId++

##### session是如何产生
接到一个连接类型的请求【详见ZooKeeperServer.processConnectRequest】

    int sessionTimeout = connReq.getTimeOut();
    byte passwd[] = connReq.getPasswd();
    int minSessionTimeout = getMinSessionTimeout();
    if (sessionTimeout < minSessionTimeout) {
        sessionTimeout = minSessionTimeout;
    }
    int maxSessionTimeout = getMaxSessionTimeout();
    if (sessionTimeout > maxSessionTimeout) {
        sessionTimeout = maxSessionTimeout;
    }
    cnxn.setSessionTimeout(sessionTimeout);
    // We don't want to receive any packets until we are sure that the
    // session is setup
    cnxn.disableRecv();
    long sessionId = connReq.getSessionId();
    if (sessionId != 0) {
        long clientSessionId = connReq.getSessionId();
        LOG.info("Client attempting to renew session 0x"
                + Long.toHexString(clientSessionId)
                + " at " + cnxn.getRemoteSocketAddress());
        serverCnxnFactory.closeSession(sessionId);
        cnxn.setSessionId(sessionId);
        reopenSession(cnxn, sessionId, passwd, sessionTimeout);
    } else {
        LOG.info("Client attempting to establish new session at "
                + cnxn.getRemoteSocketAddress());
        createSession(cnxn, passwd, sessionTimeout);
    }

###### 确定session的timeout和id

    synchronized public long createSession(int sessionTimeout) {
        addSession(nextSessionId, sessionTimeout);
        return nextSessionId++;
    }

可见产生session需要两个元素，一个是sessionid,一个是timeout
timeout由客户端确定，但必须在服务器规定的最大的timeout（ticktime*20）和最小的timeout(ticktime*2)之间
如果客户端没有指定sessionid,那么就会产生一个新的session【详见ZooKeeperServer.createSession】,否则会reopen【详见ZooKeeperServer.reopenSession】
sessionid的产生上面解释过了

###### 实例化session及相关关系存放
【详见SessionTrackerImpl.addSession】

    sessionsWithTimeout.put(id, sessionTimeout);
    if (sessionsById.get(id) == null) {
        SessionImpl s = new SessionImpl(id, sessionTimeout, 0);
        sessionsById.put(id, s);
        if (LOG.isTraceEnabled()) {
            ZooTrace.logTraceMessage(LOG, ZooTrace.SESSION_TRACE_MASK,
                    "SessionTrackerImpl --- Adding session 0x"
                    + Long.toHexString(id) + " " + sessionTimeout);
        }
    }

* 一个重要的数据结构sessionsWithTimeout存放sessionid和timeout的映射
* 另一个重要的数据结构sessionsById存放sessionid和SessionImpl实例的映射

###### 确定session实例的tickTime及sessionSets关系维护
【详见SessionTrackerImpl.touchSession】

    long expireTime = roundToInterval(System.currentTimeMillis() + timeout);
    if (s.tickTime >= expireTime) {
        // Nothing needs to be done
        return true;
    }
    SessionSet set = sessionSets.get(s.tickTime);
    if (set != null) {
        set.sessions.remove(s);
    }
    s.tickTime = expireTime;
    set = sessionSets.get(s.tickTime);
    if (set == null) {
        set = new SessionSet();
        sessionSets.put(expireTime, set);
    }
    set.sessions.add(s);

* 根据当前时间和timeout计算本session 的expireTime即tickTime
* 一个重要的数据结构sessionSets 存放过期时间和一组session实例（相同过期时间）的映射的建立及维护
* session实例的tickTime的确定

***

#### session tracker线程的机制

##### 清除session如何实现
【详见ZooKeeperServer.close】

    private void close(long sessionId) {
       submitRequest(null, sessionId, OpCode.closeSession, 0, null, null);
    }

###### 构造一个Request实例
###### 调用PrepRequestProcessor.processRequest放入submittedRequests队列
###### PrepRequestProcessor线程的处理

    request.hdr = new TxnHeader(request.sessionId, request.cxid, zxid,
                            zks.getTime(), type);
                                                    
    switch (type) {
                                                   
    //省略N行代码......
                                                   
    case OpCode.closeSession:
        // We don't want to do this check since the session expiration thread
        // queues up this operation without being the session owner.
        // this request is the last of the session so it should be ok
        //zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
        HashSet<String> es = zks.getZKDatabase()
                .getEphemerals(request.sessionId);
        synchronized (zks.outstandingChanges) {
            for (ChangeRecord c : zks.outstandingChanges) {
                if (c.stat == null) {
                    // Doing a delete
                    es.remove(c.path);
                } else if (c.stat.getEphemeralOwner() == request.sessionId) {
                    es.add(c.path);
                }
            }
            for (String path2Delete : es) {
                addChangeRecord(new ChangeRecord(request.hdr.getZxid(),
                        path2Delete, null, 0, null));
            }
                                                    
            zks.sessionTracker.setSessionClosing(request.sessionId);
        }
                                                    
        LOG.info("Processed session termination for sessionid: 0x"
                + Long.toHexString(request.sessionId));
        break;

设置request.hdr，这个很重要，

在FinalRequestProcessor.processRequest会有相应的处理

    if (request.hdr != null) {
        TxnHeader hdr = request.hdr;
        Record txn = request.txn;

        rc = zks.processTxn(hdr, txn);
    }

一旦某个session关闭，与session相关的EPHEMERAL类型的节点都得清除

并且通过调用sessionTracker.setSessionClosing将session设置为关闭，使得后续此session上的请求无效

###### SessionTrackerImpl相关数据结构的清理
【详见SessionTrackerImpl.removeSession】

    synchronized public void removeSession(long sessionId) {
        SessionImpl s = sessionsById.remove(sessionId);
        sessionsWithTimeout.remove(sessionId);
        if (LOG.isTraceEnabled()) {
            ZooTrace.logTraceMessage(LOG, ZooTrace.SESSION_TRACE_MASK,
                    "SessionTrackerImpl --- Removing session 0x"
                    + Long.toHexString(sessionId));
        }
        if (s != null) {
            sessionSets.get(s.tickTime).sessions.remove(s);
        }
    }

分别对sessionsById、sessionsWithTimeout、sessionSets进行处理

##### session owner咋回事
如果不是在集群环境，即没有LearnerHandler线程，session 的owner就是一个常量实例ServerCnxn.me

##### sessionsWithTimeout这个数据结构的用途
sessionsWithTimeout存放的是sessionid和timeout，此数据结构会和ZKDatabase中相通，会被持久化

如果某个session timeout为60s,如果空闲了30s，意味着还能空闲30s,此时服务重启，那么此session的timeout又变为60s

##### touch session
每次一旦该session有请求，就会touch，意味着session的过期时间变为（基本等于当前时间+timeout）
具体算法为

    private long roundToInterval(long time) {
        // We give a one interval grace period
        return (time / expirationInterval + 1) * expirationInterval;
    }

time为System.currentTimeMillis() + timeout
expirationInterval默认为ticktime

基本上所有的事务型操作，都会调用用来验证当前请求的session是否关闭，owner是否正确

***

#### 小结
* SessionTrackerImpl作为一个单独的线程专门处理过期session

* SessionTrackerImpl有3个重要的数据结构sessionsByIdsessionSets、sessionsWithTimeout，其中sessionsWithTimeout会被持久化

* SessionTrackerImpl提供了几个常用的API

    createSession
    addSession
    touchSession
    removeSession
    checkSession
    setOwner
    dumpSessions


