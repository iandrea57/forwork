
#### 创建线程池
Executors类，提供了一系列工厂方法用于创先线程池，返回的线程池都实现了ExecutorService接口。

    public static ExecutorService newFixedThreadPool(int nThreads)
    创建固定数目线程的线程池。
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());

    public static ExecutorService newCachedThreadPool()
    创建一个可缓存的线程池。
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());


    public static ExecutorService newSingleThreadExecutor()
    创建一个单线程化的Executor。
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
    创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类。
    return new ScheduledThreadPoolExecutor(corePoolSize)
    super(corePoolSize, Integer.MAX_VALUE, 
            0, TimeUnit.NANOSECONDS,
            new DelayedWorkQueue());
            
    private static final RejectedExecutionHandler defaultHandler =
        new AbortPolicy();


##### ThreadPoolExecutor

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {

    corePoolSize    默认线程数
    maximumPoolSize 最大线程数
    keepAliveTime   线程数检测间隔
    unit            检测时间单位  TimeUnit.MILLISECONDS
    workQueue       new ArrayBlockingQueue<Runnable>(4)   限制大小的队列
                    new LinkedBlockingQueue<Runnable>()   默认无限大小
                    new SynchronousQueue<Runnable>()  task会被直接交到thread手中，queue本身不缓存任何的task
    threadFactory   Executors.defaultThreadFactory()
    handler         AbortPolicy
                    CallerRunsPolicy
                    DiscardPolicy
                    DiscardOldestPolicy
                   