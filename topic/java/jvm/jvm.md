#### java技术体系模块图

![](http://static.open-open.com/lib/uploadImg/20120929/20120929195754_715.jpg)

#### JVM内存区域模型

![](http://static.open-open.com/lib/uploadImg/20120929/20120929195754_774.jpg)

1. 方法区

    也称"永久代” 、“非堆”，  它用于存储虚拟机加载的类信息、常量、静态变量、是各个线程共享的内存区域。默认最小值为16MB，最大值为64MB，可以通过-XX:PermSize 和 -XX:MaxPermSize 参数限制方法区的大小。

    运行时常量池：是方法区的一部分，Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译器生成的各种符号引用，这部分内容将在类加载后放到方法区的运行时常量池中。

    异常：java.lang.OutOfMemoryError: PermGen space

    引起，一般是太多的类信息，比如应用spring很多反射信息，可以适度设置大点

    方法区或永生代相关设置

    -XX:PermSize=64MB 最小尺寸，初始分配 

    -XX:MaxPermSize=256MB 最大允许分配尺寸，按需分配

    -XX:+CMSClassUnloadingEnabled 

    -XX:+CMSPermGenSweepingEnabled 设置垃圾不回收

    默认大小

    -server选项下默认MaxPermSize为64m 

    -client选项下默认MaxPermSize为32m

2. 虚拟机栈

    描述的是java 方法执行的内存模型：每个方法被执行的时候 都会创建一个“栈帧”用于存储局部变量表(包括参数)、操作栈、方法出口等信息。每个方法被调用到执行完的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。声明周期与线程相同，是线程私有的。

    局部变量表存放了编译器可知的各种基本数据类型(boolean、byte、char、short、int、float、long、 double)、对象引用(引用指针，并非对象本身)，其中64位长度的long和double类型的数据会占用2个局部变量的空间，其余数据类型只占1 个。局部变量表所需的内存空间在编译期间完成分配，当进入一个方法时，这个方法需要在栈帧中分配多大的局部变量是完全确定的，在运行期间栈帧不会改变局部 变量表的大小空间。

    异常 ：Fatal: Stack size too small

3. 本地方法栈

    与虚拟机栈基本类似，区别在于虚拟机栈为虚拟机执行的java方法服务，而本地方法栈则是为Native方法服务。

    异常：java.lang.OutOfMemoryError: unable to create new native thread

4. 堆 
    也叫做java 堆、GC堆是java虚拟机所管理的内存中最大的一块内存区域，也是被各个线程共享的内存区域，在JVM启动时创建。该内存区域存放了对象实例及数组(所有new的对象)。其大小通过-Xms(最小值)和-Xmx(最大值)参数设置，-Xms为JVM启动时申请的最小内存，默认为操作系统物理内存的1/64但小于1G，-Xmx为JVM可申请的最大内存，默认为物理内存的1/4但小于1G，默认当空余堆内存小于40%时，JVM会增大Heap到-Xmx指定的大小，可通过-XX:MinHeapFreeRation=来指定这个比列；当空余堆内存大于70%时，JVM会减小heap的大小到-Xms指定的大小，可通过XX:MaxHeapFreeRation=来指定这个比列，对于运行系统，为避免在运行时频繁调整Heap的大小，通常-Xms与-Xmx的值设成一样。

    由于现在收集器都是采用分代收集算法，堆被划分为新生代和老年代。新生代主要存储新创建的对象和尚未进入老年代的对象。老年代存储经过多次新生代GC(Minor GC)任然存活的对象。

    新生代：

    程序新创建的对象都是从新生代分配内存，新生代由Eden Space和两块相同大小的Survivor Space(通常又称S0和S1或From和To)构成，可通过-Xmn参数来指定新生代的大小，也可以通过-XX:SurvivorRation来调整Eden Space及Survivor Space的大小。

    老年代：

    用于存放经过多次新生代GC任然存活的对象，例如缓存对象，新建的对象也有 可能直接进入老年代，主要有两种情况：①.大对象，可通过启动参数设置-XX:PretenureSizeThreshold=1024(单位为字节，默 认为0)来代表超过多大时就不在新生代分配，而是直接在老年代分配。②.大的数组对象，切数组中无引用外部对象。

    老年代所占的内存大小为-Xmx对应的值减去-Xmn对应的值。

5. 程序计数器
 
    它的作用是当前线程所执行的字节码的行号指示器，在虚拟机的模型里，字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、异常处理、线程恢复等基础功能都需要依赖计数器完成。

![](http://img.my.csdn.net/uploads/201209/30/1349009313_6062.jpg)

#### 直接内存
直接内存并不是虚拟机内存的一部分，也不是Java虚拟机规范中定义的内存区域。jdk1.4中新加入的NIO，引入了通道与缓冲区的IO方式，它可以调用Native方法直接分配堆外内存，这个堆外内存就是本机内存，不会影响到堆内存的大小。


#### 垃圾回收
##### The New Generational Heap
默认4M，此区域一般为JVM内存的1/15大小

此代分为Eden space区，Survivo space区可以看成emptySurvivo区，Survivor区,

当new 一个对象时，首先是在Eden space区，当Eden space区满时，Survivor space区进行垃圾回收（此处复制算法），当对象在Survivo space区经过几次回收Tenured Generation

复制算法，每次算法开始都得停止当前所有的线程，然后把Survivor区的所有活跃的对象复制到emptySurvivo区，然后对Survivor区空间进行清除变成emptySurvivo，以前的emptySurvivo成为了Survivor区。（互换）

##### Tenured Generation
此处储存年老代的对象，此处的垃圾回收采用The Mark and Sweep 算法

GC标记算法/清理算法（The Mark and Sweep algorithms）进行回收，从引用进行标记，然后按照引用的程度或无引用到的对象进行回收，然后再对清除了的内存进行合并.

##### GC策略介绍
1. serial collector，单线程收集器，回收时都需要暂停当前线程，长时间等待，

    Client下默认方式
    
    强制加上 -XX:+UseSerialGC

2. parallel collector( throughput collector )，并行收集器，或叫多线程的收集，

    年轻代：暂停应用程序，多个垃圾收集线程并行的复制收集。
    年老代：暂停应用程序, 多个垃圾收集线程并行的复制收集。

    server下默认方式，具体配置

    设置并行收集的线程数目，如20个线程，-XX:ParallelGCThreads=20
    
    配置年轻代为并行收集  -XX:+UseParallelGC.
    
    配置年老代垃圾收集方式为并行收集（JDK6.0开始支持）-XX:+UseParallelOldGC
    
    设置暂停时间，设置每次年轻代垃圾回收的最长时间如果无法满足此时间，JVM会自动调整年轻代大小，以满足此值，如 -XX:MaxGCPauseMillis= 100， 
    
    设置吞吐量，吞吐量为垃圾回收时间与非垃圾回收时间的比值  -XX:GCTimeRatio 来调整GC的时间

3. concurrent collector(concurrent low pause collector)，并发收集器，

    年轻代：同样是暂停应用程序，多个垃圾收集线程并行的复制收集。
    年老代：和并行的区别在这，只是在初始标记（initial mark）和二次标记（remark）时需要stop-the-world。但收集时时间很长，所以不能等年轻代满后再开始清理.
    
    使用启动并发收集器 -XX:+UseConcMarkSweepGC
    
    XX:CMSInitiatingOccupancyFraction=指定还有多少剩余堆时开始执行并发收集
        
    根据官方文档，他们俩个需要在多CPU的情况下，才能发挥作用。在一个CPU的情况下，会不如默认的serial collector，因为线程管理需要耗费CPU资源。而在两个CPU的情况下，也提高不大。只是在更多CPU的情况下，才会有所提高。当然 concurrent low pause collector有一种模式可以在CPU较少的机器上，提供尽可能少的停顿的模式，见CMS GC Incremental mode。

    当要使用throughput collector时，在java opt里加上-XX:+UseParallelGC，启动throughput collector收集。也可加上-XX:ParallelGCThreads=<desired number>来改变线程数。还有两个参数 -XX:MaxGCPauseMillis=<nnn>和 -XX:GCTimeRatio=<nnn>，MaxGCPauseMillis=<nnn>用来控制最大暂停时间，而-XX: GCTimeRatio可以提高GC说占CPU的比，以最大话的减小heap。

##### 堆内存的相关参数设置

默认值（基于-server）

-server时最大堆内存是物理内存的1/4，但小于1G. JDK 1.5以前是64M

（官方： Smaller of 1/4th of the physical memory or 1GB.Before J2SE 5.0, the default maximum heap size was 64MB.）

-client 小一倍

参数设置

    -Xms128m 
    表示JVM Heap(堆内存)最小尺寸128MB，初始分配 

    -Xmx512m 
    表示JVM Heap(堆内存)最大允许的尺寸256MB，按需分配

    new Generation与Old Generation的比例，默认为1:2，即为2

    -XX:NewRatio= 参数可以设置（也可以-XX:NewSize和-XX:MaxNewsize设置新域的初始值和最大值）

    Eden与Survivor的比例，默认为32

    -XX:SurvivorRation=参数可以设置

    当对象默认经过1次New Generation 就转入Old Generation（这个不同文章上不同，待我确定）

    -XX:MaxTenuringThreshold=参数可以设置 （默认0）

    用-XX:+PrintTenuringDistributio可以查看值


#### GC

![](http://img.my.csdn.net/uploads/201301/02/1357139665_9009.png)

##### 内存申请过程

1. JVM会试图为相关Java对象在Eden中初始化一块内存区域；

2. 当Eden空间足够时，内存申请结束。否则到下一步；

3. JVM试图释放在Eden中所有不活跃的对象（minor collection），释放后若Eden空间仍然不足以放入新对象，则试图将部分Eden中活跃对象放入Survivor区；

4. Survivor区被用来作为Eden及old的中间交换区域，当OLD区空间足够时，Survivor区的对象会被移到Old区，否则会被保留在Survivor区；

5. 当old区空间不够时，JVM会在old区进行major collection；

6. 完全垃圾收集后，若Survivor及old区仍然无法存放从Eden复制过来的部分对象，导致JVM无法在Eden区为新对象创建内存区域，则出现"Out of memory错误"；

##### 对象衰老过程
新创建的对象的内存都分配自eden。Minor collection的过程就是将eden和在用survivor space中的活对象copy到空闲survivor space中。对象在young generation里经历了一定次数(可以通过参数配置)的minor collection后，就会被移到old generation中，称为tenuring。


#### GC触发基本过程

1. 如果Eden空间占满了， 会触发 minor GC。 Minor GC 后仍然存活的对象会被复制到S0中去。这样Eden就被清空可以分配给新的对象。

2. 又触发了一次 Minor GC ， S0和Eden中存活的对象被复制到S1中， 并且S0和Eden被清空。 在同一时刻, 只有Eden和一个Survivor Space同时被操作。

3. 当每次对象从Eden复制到Survivor Space或者从Survivor Space中的一个复制到另外一个，有一个计数器会自动增加值。 默认情况下如果复制发生超过16次， JVM 会停止复制并把他们移到老年代中去。

4. 如果一个对象不能在Eden中被创建，它会直接被创建在老年代中。 如果老年代的空间被占满会触发老年代的 GC，也被称为 Full GC。full GC 是一个压缩处理过程，所以它比Minor GC要慢很多。



