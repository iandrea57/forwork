

##### zkCli.sh
    sh zkCli.sh -server 10.4.30.143:2181,10.4.30.144:2181,10.4.30.144:2183

##### connectString
    host(:port)?(,host(:port)?)*(/rootpath)?

***

##### timeout
    客户端
    connectTimeout = sessionTimeout / hostProvider.size();
    readTimeout = sessionTimeout * 2 / 3;
    
    服务器
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
    
    public int getMinSessionTimeout() {
        return minSessionTimeout == -1 ? tickTime * 2 : minSessionTimeout;
    }

    public int getMaxSessionTimeout() {
        return maxSessionTimeout == -1 ? tickTime * 20 : maxSessionTimeout;
    }

***

##### EventType
* -1: None
* 1: NodeCreated
* 2: NodeDeleted
* 3: NodeDataChanged
* 4: NodeChildrenChanged

***

##### KeeperState
* -1: Unknown *@Deprecated*
* 0: Disconnected
* 1: NoSyncConnected *@Deprecated*
* 3: SyncConnected
* 4: AuthFailed
* 5: ConnectedReadOnly
* 6: SaslAuthenticated
* -112: Expired