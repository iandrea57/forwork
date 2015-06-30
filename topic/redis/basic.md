
#### redis和memcache的不同

1. 存储方式：

    memecache 把数据全部存在内存之中，断电后会挂掉，数据不能超过内存大小
    
    redis有部份存在硬盘上，这样能保证数据的持久性，支持数据的持久化（笔者注：有快照和AOF日志两种持久化方式，在实际应用的时候，要特别注意配置文件快照参数，要不就很有可能服务器频繁满载做dump）。

2. 数据支持类型：

    redis在数据支持上要比memecache多的多。

3. 使用底层模型不同：
    
    新版本的redis直接自己构建了VM 机制 ，因为一般的系统调用系统函数的话，会浪费一定的时间去移动和请求。
    
4. 运行环境不同：
    
    redis目前官方只支持LINUX

个人总结一下，有持久化需求或者对数据结构和处理有高级要求的应用，选择redis，其他简单的key/value存储，选择memcache。


1、 Redis和Memcache都是将数据存放在内存中，都是内存数据库。不过memcache还可用于缓存其他东西，例如图片、视频等等。

2、Redis不仅仅支持简单的k/v类型的数据，同时还提供string, hash, list, set, zset等数据结构的存储。

3、虚拟内存--Redis当物理内存用完时，可以将一些很久没用到的value 交换到磁盘

4、过期策略--memcache在set时就指定，例如set key1 0 0 8,即永不过期。Redis可以通过例如expire 设定，例如expire name 10

5、分布式--设定memcache集群，利用magent做一主多从;redis可以做一主多从。都可以一主一从

6、存储数据安全--memcache挂掉后，数据没了；redis可以定期保存到磁盘（持久化）

7、灾难恢复--memcache挂掉后，数据不可恢复; redis数据丢失后可以通过aof恢复

8、Redis支持数据的备份，即master-slave模式的数据备份。


1、redis具有持久化机制，可以定期将内存中的数据持久化到硬盘上。

2、redis具备binlog功能，可以将所有操作写入日志，当redis出现故障，可依照binlog进行数据恢复。

3、redis支持virtual memory，可以限定内存使用大小，当数据超过阈值，则通过类似LRU的算法把内存中的最不常用数据保存到硬盘的页面文件中。

4、redis原生支持的数据类型更多，使用的想象空间更大。

5、前面有位朋友所提及的一致性哈希，用在redis的sharding中，一般是在负载非常高需要水平扩展时使用。我们还没有用到这方面的功能，一般的项目，单机足够支撑并发了。redis 3.0将推出cluster，功能更加强大。

6、redis更多优点，请移步官方网站查询。


下面内容来自Redis作者在stackoverflow上的一个回答，对应的问题是《Is memcached a dinosaur in comparison to Redis?》（相比Redis，Memcached真的过时了吗？）

You should not care too much about performances. Redis is faster per core with small values, but memcached is able to use multiple cores with a single executable and TCP port without help from the client. Also memcached is faster with big values in the order of 100k. Redis recently improved a lot about big values (unstable branch) but still memcached is faster in this use case. The point here is: nor one or the other will likely going to be your bottleneck for the query-per-second they can deliver.

没有必要过多的关心性能，因为二者的性能都已经足够高了。由于Redis只使用单核，而Memcached可以使用多核，所以在比较上，平均每一个核上Redis在存储小数据时比Memcached性能更高。而在100k以上的数据中，Memcached性能要高于Redis，虽然Redis最近也在存储大数据的性能上进行优化，但是比起Memcached，还是稍有逊色。说了这么多，结论是，无论你使用哪一个，每秒处理请求的次数都不会成为瓶颈。（比如瓶颈可能会在网卡）

You should care about memory usage. For simple key-value pairs memcached is more memory efficient. If you use Redis hashes, Redis is more memory efficient. Depends on the use case.

如果要说内存使用效率，使用简单的key-value存储的话，Memcached的内存利用率更高，而如果Redis采用hash结构来做key-value存储，由于其组合式的压缩，其内存利用率会高于Memcached。当然，这和你的应用场景和数据特性有关。

You should care about persistence and replication, two features only available in Redis. Even if your goal is to build a cache it helps that after an upgrade or a reboot your data are still there.

如果你对数据持久化和数据同步有所要求，那么推荐你选择Redis，因为这两个特性Memcached都不具备。即使你只是希望在升级或者重启系统后缓存数据不会丢失，选择Redis也是明智的。

You should care about the kind of operations you need. In Redis there are a lot of complex operations, even just considering the caching use case, you often can do a lot more in a single operation, without requiring data to be processed client side (a lot of I/O is sometimes needed). This operations are often as fast as plain GET and SET. So if you don’t need just GEt/SET but more complex things Redis can help a lot (think at timeline caching).

当然，最后还得说到你的具体应用需求。Redis相比Memcached来说，拥有更多的数据结构和并支持更丰富的数据操作，通常在Memcached里，你需要将数据拿到客户端来进行类似的修改再set回去。这大大增加了网络IO的次数和数据体积。在Redis中，这些复杂的操作通常和一般的GET/SET一样高效。所以，如果你需要缓存能够支持更复杂的结构和操作，那么Redis会是不错的选择。
