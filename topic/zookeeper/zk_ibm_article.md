### zookeeper
Zookeeper 分布式服务框架是 Apache Hadoop 的一个子项目，作为分布式应用程序协调服务，它主要是用来解决分布式应用中经常遇到的一些数据管理问题，如：统一命名服务、状态同步服务、集群管理、分布式应用配置项的管理等。


#### zoo.cfg
    tickTime   服务器之间或客户端与服务器之间维持心跳的时间间隔
    dataDir    保存数据的目录
    clientPort Zookeeper会监听这个端口，接受客户端的访问请求
    initLimit  Leader与Follower初始化连接时最长能忍受多少个心跳时间间隔数
    syncLimit  Leader与Follower之间发送消息，请求和应答能忍受多少个心跳时间间隔数
    server.A=B:C:D   其中 
    A 是一个数字，表示这个是第几号服务器；
    B 是这个服务器的 ip 地址；
    C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；
    D 这个端口就是用来执行选举Leader时服务器相互通信的端口。
如果是伪集群的配置方式，由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，所以要给它们分配不同的端口号。

除了修改 zoo.cfg 配置文件，集群模式下还要配置一个文件 myid，这个文件在 dataDir 目录下，这个文件里面就有一个数据就是 A 的值，Zookeeper 启动时会读取这个文件，拿到里面的数据与 zoo.cfg 里面的配置信息比较从而判断到底是那个 server。

***
#### 数据模型
Zookeeper 会维护一个具有层次关系的数据结构，它非常类似于一个标准的文件系统

![](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/image001.gif "zk数据结构")

1. 每个子目录项如 NameService 都被称作为 znode，这个 znode 是被它所在的路径唯一标识，如 Server1 这个 znode 的标识为 /NameService/Server1
2. znode 可以有子节点目录，并且每个 znode 可以存储数据，注意 EPHEMERAL 类型的目录节点不能有子节点目录
3. znode 是有版本的，每个 znode 中存储的数据可以有多个版本，也就是一个访问路径中可以存储多份数据
4. znode 可以是临时节点，一旦创建这个 znode 的客户端与服务器失去联系，这个 znode 也将自动删除，Zookeeper 的客户端和服务器通信采用长连接方式，每个客户端和服务器通过心跳来保持连接，这个连接状态称为 session，如果 znode 是临时节点，这个 session 失效，znode 也就删除了
5. znode 的目录名可以自动编号，如 App1 已经存在，再创建的话，将会自动命名为 App2
6. znode 可以被监控，包括这个目录节点中存储的数据的修改，子节点目录的变化等，一旦变化可以通知设置监控的客户端，这个是 Zookeeper 的核心特性，Zookeeper 的很多功能都是基于这个特性实现的，后面在典型的应用场景中会有实例介绍
***
#### 如何使用
Zookeeper 作为一个分布式的服务框架，主要用来解决**分布式集群中应用系统的一致性问题**，它能提供基于类似于文件系统的目录节点树方式的数据存储，但是 Zookeeper 并不是用来专门存储数据的，它的作用主要是用来维护和监控你存储的数据的状态变化。通过监控这些数据状态的变化，从而可以达到基于数据的集群管理
***
#### 节点模式
* PERSISTENT：持久化目录节点，这个目录节点存储的数据不会丢失；
* PERSISTENT_SEQUENTIAL：顺序自动编号的目录节点，这种目录节点会根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名；
* **EPHEMERAL**：临时目录节点，一旦创建这个节点的客户端与服务器端口也就是 session 超时，这种节点会被自动删除；
* EPHEMERAL_SEQUENTIAL：临时自动编号节点
***
#### 基本操作

     // 创建一个与服务器的连接
     ZooKeeper zk = new ZooKeeper("localhost:" + CLIENT_PORT, 
            ClientBase.CONNECTION_TIMEOUT, new Watcher() { 
                // 监控所有被触发的事件
                public void process(WatchedEvent event) { 
                    System.out.println("已经触发了" + event.getType() + "事件！"); 
                } 
            }); 
     // 创建一个目录节点
     zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,
       CreateMode.PERSISTENT); 
     // 创建一个子目录节点
     zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
       Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
     System.out.println(new String(zk.getData("/testRootPath",false,null))); 
     // 取出子目录节点列表
     System.out.println(zk.getChildren("/testRootPath",true)); 
     // 修改子目录节点数据
     zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1); 
     System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]"); 
     // 创建另外一个子目录节点
     zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), 
       Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
     System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null))); 
     // 删除子目录节点
     zk.delete("/testRootPath/testChildPathTwo",-1); 
     zk.delete("/testRootPath/testChildPathOne",-1); 
     // 删除父目录节点
     zk.delete("/testRootPath",-1); 
     // 关闭连接
     zk.close();
***
#### ZooKeeper 典型的应用场景
Zookeeper 从设计模式角度来看，是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都关心的数据，然后接受观察者的注册，一旦这些数据的状态发生变化，Zookeeper 就将负责通知已经在 Zookeeper 上注册的那些观察者做出相应的反应

##### **统一命名服务（Name Service）**
分布式应用中，通常需要有一套完整的命名规则，既能够产生唯一的名称又便于人识别和记住
##### **配置管理（Configuration Management）**
配置的管理在分布式应用环境中很常见，例如同一个应用系统需要多台 PC Server 运行，但是它们运行的应用系统的某些配置项是相同的，如果要修改这些相同的配置项，那么就必须同时修改每台运行这个应用系统的 PC Server，这样非常麻烦而且容易出错。

像这样的配置信息完全可以交给 Zookeeper 来管理，将配置信息保存在 Zookeeper 的某个目录节点中，然后将所有需要修改的应用机器监控配置信息的状态，一旦配置信息发生变化，每台应用机器就会收到 Zookeeper 的通知，然后从 Zookeeper 获取新的配置信息应用到系统中。

![](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/image002.gif "配置管理结构图")

##### **集群管理（Group Membership）**
Zookeeper 能够很容易的实现集群管理的功能，如有多台 Server 组成一个服务集群，那么必须要一个“总管”知道当前集群中每台机器的服务状态，一旦有机器不能提供服务，集群中其它集群必须知道，从而做出调整重新分配服务策略。同样当增加集群的服务能力时，就会增加一台或多台 Server，同样也必须让“总管”知道。

Zookeeper 不仅能够帮你维护当前的集群中机器的服务状态，而且能够帮你选出一个“总管”，让这个总管来管理集群，这就是 Zookeeper 的另一个功能 Leader Election。

它们的实现方式都是在 Zookeeper 上创建一个 EPHEMERAL 类型的目录节点，然后每个 Server 在它们创建目录节点的父目录节点上调用 getChildren(String path, boolean watch) 方法并设置 watch 为 true，由于是 EPHEMERAL 目录节点，当创建它的 Server 死去，这个目录节点也随之被删除，所以 Children 将会变化，这时 getChildren上的 Watch 将会被调用，所以其它 Server 就知道已经有某台 Server 死去了。新增 Server 也是同样的原理。

Zookeeper 如何实现 Leader Election，也就是选出一个 Master Server。和前面的一样每台 Server 创建一个 EPHEMERAL 目录节点，不同的是它还是一个 SEQUENTIAL 目录节点，所以它是个 EPHEMERAL_SEQUENTIAL 目录节点。之所以它是 EPHEMERAL_SEQUENTIAL 目录节点，是因为我们可以给每台 Server 编号，我们可以选择当前是最小编号的 Server 为 Master，假如这个最小编号的 Server 死去，由于是 EPHEMERAL 节点，死去的 Server 对应的节点也被删除，所以当前的节点列表中又出现一个最小编号的节点，我们就选择这个节点为当前 Master。这样就实现了动态选择 Master，避免了传统意义上单 Master 容易出现单点故障的问题。

![](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/image003.gif "集群管理结构图")

##### **共享锁（Locks）**
共享锁在同一个进程中很容易实现，但是在跨进程或者在不同 Server 之间就不好实现了。Zookeeper 却很容易实现这个功能，实现方式也是需要获得锁的 Server 创建一个 EPHEMERAL_SEQUENTIAL 目录节点，然后调用 getChildren方法获取当前的目录节点列表中最小的目录节点是不是就是自己创建的目录节点，如果正是自己创建的，那么它就获得了这个锁，如果不是那么它就调用 exists(String path, boolean watch) 方法并监控 Zookeeper 上目录节点列表的变化，一直到自己创建的节点是列表中最小编号的目录节点，从而获得锁，释放锁很简单，只要删除前面它自己所创建的目录节点就行了。

![](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/image004.gif "Zookeeper 实现 Locks 的流程图")

同步锁关键代码

    void getLock() throws KeeperException, InterruptedException{ 
        List<String> list = zk.getChildren(root, false); 
        String[] nodes = list.toArray(new String[list.size()]); 
        Arrays.sort(nodes); 
        if(myZnode.equals(root+"/"+nodes[0])){ 
            doAction(); 
        } 
        else{ 
            waitForLock(nodes[0]); 
        } 
    } 
    void waitForLock(String lower) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(root + "/" + lower,true); 
        if(stat != null){ 
            mutex.wait(); 
        } 
        else{ 
            getLock(); 
        } 
    }

##### **队列管理**
Zookeeper 可以处理两种类型的队列：

1. 当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达，这种是同步队列。
2. 队列按照 FIFO 方式进行入队和出队操作，例如实现生产者和消费者模型。

同步队列用 Zookeeper 实现的实现思路如下：

创建一个父目录 /synchronizing，每个成员都监控标志（Set Watch）位目录 /synchronizing/start 是否存在，然后每个成员都加入这个队列，加入队列的方式就是创建 /synchronizing/member_i 的临时目录节点，然后每个成员获取 / synchronizing 目录的所有目录节点，也就是 member_i。判断 i 的值是否已经是成员的个数，如果小于成员个数等待 /synchronizing/start 的出现，如果已经相等就创建 /synchronizing/start。

![](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/image005.gif "同步队列流程图")

同步队列的关键代码如下

     void addQueue() throws KeeperException, InterruptedException{ 
            zk.exists(root + "/start",true); 
            zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE, 
            CreateMode.EPHEMERAL_SEQUENTIAL); 
            synchronized (mutex) { 
                List<String> list = zk.getChildren(root, false); 
                if (list.size() < size) { 
                    mutex.wait(); 
                } else { 
                    zk.create(root + "/start", new byte[0], Ids.OPEN_ACL_UNSAFE,
                     CreateMode.PERSISTENT); 
                } 
            } 
     }

当队列没满是进入 wait()，然后会一直等待 Watch 的通知，Watch 的代码如下：

     public void process(WatchedEvent event) { 
            if(event.getPath().equals(root + "/start") &&
             event.getType() == Event.EventType.NodeCreated){ 
                System.out.println("得到通知"); 
                super.process(event); 
                doAction(); 
            } 
        }

FIFO 队列用 Zookeeper 实现思路如下：

实现的思路也非常简单，就是在特定的目录下创建 SEQUENTIAL 类型的子目录 /queue_i，这样就能保证所有成员加入队列时都是有编号的，出队列时通过 getChildren( ) 方法可以返回当前所有的队列中的元素，然后消费其中最小的一个，这样就能保证 FIFO。

下面是生产者和消费者这种队列形式的示例代码

生产者代码

     boolean produce(int i) throws KeeperException, InterruptedException{ 
            ByteBuffer b = ByteBuffer.allocate(4); 
            byte[] value; 
            b.putInt(i); 
            value = b.array(); 
            zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                        CreateMode.PERSISTENT_SEQUENTIAL); 
            return true; 
        }

消费者代码

    int consume() throws KeeperException, InterruptedException{ 
            int retvalue = -1; 
            Stat stat = null; 
            while (true) { 
                synchronized (mutex) { 
                    List<String> list = zk.getChildren(root, true); 
                    if (list.size() == 0) { 
                        mutex.wait(); 
                    } else { 
                        Integer min = new Integer(list.get(0).substring(7)); 
                        for(String s : list){ 
                            Integer tempValue = new Integer(s.substring(7)); 
                            if(tempValue < min) min = tempValue; 
                        } 
                        byte[] b = zk.getData(root + "/element" + min,false, stat); 
                        zk.delete(root + "/element" + min, 0); 
                        ByteBuffer buffer = ByteBuffer.wrap(b); 
                        retvalue = buffer.getInt(); 
                        return retvalue; 
                    } 
                } 
            } 
     }


