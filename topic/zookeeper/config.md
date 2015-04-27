
* clientPort

    客户端连接server的端口，即zk对外服务端口，一般设置为2181。

* dataDir

    就是把内存中的数据存储成快照文件snapshot的目录，同时myid也存储在这个目录下（myid中的内容为本机server服务的标识）。写快照不需要单独的磁盘，而且是使用后台线程进行异步写数据到磁盘，因此不会对内存数据有影响。默认情况下，事务日志也会存储在这里。建议同时配置参数dataLogDir, 事务日志的写性能直接影响zk性能。

* tickTime

    ZK中的一个时间单元。ZK中所有时间都是以这个时间单元为基础，进行整数倍配置的。例如，session的最小超时时间是2*tickTime。默认3000毫秒。这个单元时间不能设置过大或过小，过大会加大超时时间，也就加大了集群检测session失效时间；设置过小会导致session很容易超时，并且会导致网络通讯负载较重（心跳时间缩短）

* dataLogDir

    事务日志输出目录。尽量给事务日志的输出配置单独的磁盘或是挂载点，这将极大的提升ZK性能。 由于事务日志输出时是顺序且同步写到磁盘，只有从磁盘写完日志后才会触发follower和leader发回事务日志确认消息（zk事务采用两阶段提交），因此需要单独磁盘避免随机读写和磁盘缓存导致事务日志写入较慢或存储在缓存中没有写入。

* globalOutstandingLimit

    最大请求堆积数。默认是1000。ZK运行的时候， 尽管server已经没有空闲来处理更多的客户端请求了，但是还是允许客户端将请求提交到服务器上来，以提高吞吐性能。当然，为了防止Server内存溢出，这个请求堆积数还是需要限制下的。当有非常多的客户端并且请求都比较大时，可以减少这个值，不过这种情况很少。 (Java system property:zookeeper.globalOutstandingLimit)

* preAllocSize

    预先开辟磁盘空间，用于后续写入事务日志。默认是64M，每个事务日志大小就是64M，这个默认大小是按snapCount为100000且每个事务信息为512b来计算的。如果ZK的快照频率较大的话，建议适当减小这个参数。(Java system property:zookeeper.preAllocSize)。当事务日志文件不会增长得太大的话，这个大小是可以减小的。比如1000次事务会新产生一个快照（参数为snapCount），新产生快照后会用新的事务日志文件，假设一个事务信息大小100b，那么事务日志预开辟的大小为100kb会比较好。

* snapCount

    每进行snapCount次事务日志输出后，触发一次快照(snapshot), 此时，ZK会生成一个snapshot.*文件，同时创建一个新的事务日志文件log.*。默认是100000.（真正的代码实现中，会进行一定的随机数处理，以避免所有服务器在同一时间进行快照而影响性能）(Java system property:zookeeper.snapCount)。在通过快照和事务日志恢复数据时，使用的时间为读取快照时间和读取在这个快照之后产生的事务日志的时间，因此snapCount太大会导致读取事务日志的数量较多，snapCount较小会导致产生快照文件很多。

* traceFile

    用于记录所有请求的log，一般调试过程中可以使用，但是生产环境不建议使用，会严重影响性能。(Java system property:requestTraceFile)

* maxClientCnxns

    单个客户端与单台服务器之间的连接数的限制，是ip级别的，默认是60，如果设置为0，那么表明不作任何限制。请注意这个限制的使用范围，仅仅是单台客户端机器与单台ZK服务器之间的连接数限制，不是针对指定客户端IP，也不是ZK集群的连接数限制，也不是单台ZK对所有客户端的连接数限制。指定客户端IP的限制策略，这里有一个patch，可以尝试一下：http://rdc.taobao.com/team/jm/archives/1334（No Java system property）

* clientPortAddress

    对于多网卡的机器，可以为每个IP指定不同的监听端口。默认情况是所有IP都监听clientPort指定的端口。New   in 3.3.0

* minSessionTimeoutmaxSessionTimeout

    Session超时时间限制，如果客户端设置的超时时间不在这个范围，那么会被强制设置为最大或最小时间。默认的Session超时时间是在2 * tickTime ~ 20 * tickTime这个范围 New   in 3.3.0

* fsync.warningthresholdms

    事务日志输出时，如果调用fsync方法超过指定的超时时间，那么会在日志中输出警告信息。默认是1000ms。(Java system property:fsync.warningthresholdms) New in 3.3.4

*   autopurge.purgeInterval

    在上文中已经提到，3.4.0及之后版本，ZK提供了自动清理事务日志和快照文件的功能，这个参数指定了清理频率，单位是小时，需要配置一个1或更大的整数，默认是0，表示不开启自动清理功能，但可以运行bin/zkCleanup.sh来手动清理zk日志。(No Java system property) New in 3.4.0

* autopurge.snapRetainCount

    这个参数和上面的参数搭配使用，这个参数指定了需要保留的文件数目。默认是保留3个。(No Java system property) New in 3.4.0

* electionAlg

    在之前的版本中， 这个参数配置是允许我们选择leader选举算法，但是由于在以后的版本中，只会留下一种“TCP-based version of fast leader   election”算法，所以这个参数目前看来没有用了，这里也不详细展开说了。(No Java system property)

* initLimit

    Follower在启动过程中，会从Leader同步所有最新数据，然后确定自己能够对外服务的起始状态。Leader允许Follower在initLimit时间内完成这个工作。通常情况下，我们不用太在意这个参数的设置。如果ZK集群的数据量确实很大了，Follower在启动的时候，从Leader上同步数据的时间也会相应变长，因此在这种情况下，有必要适当调大这个参数了。默认值为10，即10 * tickTime  (No Java system property)

* syncLimit

    在运行过程中，Leader负责与ZK集群中所有机器进行通信，例如通过一些心跳检测机制，来检测机器的存活状态。如果Leader发出心跳包在syncLimit之后，还没有从Follower那里收到响应，那么就认为这个Follower已经不在线了。注意：不要把这个参数设置得过大，否则可能会掩盖一些问题，设置大小依赖与网络延迟和吞吐情况。默认为5，即5 * tickTime (No Java system   property)

* leaderServes

    默认情况下，Leader是会接受客户端连接，并提供正常的读写服务。但是，如果你想让Leader专注于集群中机器的协调，那么可以将这个参数设置为no，这样一来，会大大提高写操作的性能。默认为yes(Java system property:   zookeeper.leaderServes)。

* server.x=[hostname]:n:n

    这里的x是一个数字，与myid文件中的id是一致的，用来标识这个zk server，大小为1-255。右边可以配置两个端口，第一个端口用于Follower和Leader之间的数据同步和其它通信，第二个端口用于Leader选举过程中投票通信。Zk启动时，会读取myid中的值，从而得到server.x的配置为本机配置，并且也可以通过这个id找到和其他zk通信的地址和端口。hostname为机器ip，第一个端口n为事务发送的通信端口，第二个n为leader选举的通信端口，默认为2888:3888。(No Java system property)

    group.x=nnnnn[:nnnnn]

    weight.x=nnnnn

    对机器分组和权重设置，可以 参见这里(No Java system property)

* cnxTimeout

    Leader选举过程中，打开一次连接（选举的server互相通信建立连接）的超时时间，默认是5s。(Java system property: zookeeper.cnxTimeout)

    zookeeper.DigestAuthenticationProvider.superDigest

    ZK权限设置相关，具体参见《使用super身份对有权限的节点进行操作》 和 《ZooKeeper权限控制》

* skipACL

    对所有客户端请求都不作ACL检查。如果之前节点上设置有权限限制，一旦服务器上打开这个开头，那么也将失效。(Java system property:zookeeper.skipACL)

* forceSync

    这个参数确定了是否需要在事务日志提交的时候调用FileChannel.force来保证数据完全同步到磁盘。(Java system property:zookeeper.forceSync)

* jute.maxbuffer

    每个节点最大数据量，是默认是1M。这个限制必须在server和client端都进行设置才会生效。(Java system property:jute.maxbuffer)

    server.x:hostname:n:n:observer

    配置observer，表示本机是一个观察者（观察者不参与事务和选举，但会转发更新请求给leader）。比如：server.4:localhost:2181:3181:observer

* peerType=observer

    结合上面一条配置，表示这个zookeeper为观察者