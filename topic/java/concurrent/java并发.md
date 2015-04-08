### 并发简史
#### 进程间交换数据通信机制
* 套接字
* 信号处理器
* 共享内存
* 信号量
* 文件
***

#### 线程封闭
* 如果仅在单线程内访问数据，就不需要同步。这种技术称为线程封闭。它是线程安全最简单方式之一
eg:JDBC 的Connection对象，servlet请求大部分都是单线程同步方式处理，并且在Connection对象返回之前，连接池不会再将它分配给其它线程。
***

#### BlockingQueue的核心方法：
##### 放入数据：
* offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）

* offer(E o, long timeout, TimeUnit unit),可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。

* put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
##### 获取数据：
* poll(time):取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null;　　　　

* poll(long timeout, TimeUnit unit)：从BlockingQueue取出一个队首的对象，如果在指定时间内，队列一旦有数据可取，则立即返回队列中的数据。否则知道时间超时还没有数据可取，返回失败。

* take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到BlockingQueue有新的数据被加入; 

* drainTo():一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数），通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。

***
#### 生产者消费者
    public class BoundedBuffer {
    
        private LinkedList<Object> list = new LinkedList<Object>();
    
        final Lock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();
    
        int maxCount = 100;
    
        public BoundedBuffer() {
    
        }
    
        public BoundedBuffer(int maxCount) {
            this.maxCount = maxCount;
        }
    
        public void put(Object pidModel) {
            lock.lock();
            try {
                while (maxCount == list.size()) {
                    System.out.println("队列是满的...");
                    notFull.await();
                }
                list.addLast(pidModel);
                notEmpty.signal();
            } catch (InterruptedException ie) {
    
            } finally {
                lock.unlock();
            }
        }
    
        public Object take() {
            Object result = null;
            lock.lock();
            try {
                while (list.isEmpty()) {
                    System.out.println("队列是空的...");
                    notEmpty.await();
                }
                result = list.removeFirst();
                notFull.signal();
            } catch (InterruptedException ie) {
    
            } finally {
                lock.unlock();
            }
            return result;
        }
    }

***

#### java.util.concurrent 的结构
![alter text](http://img.blog.csdn.net/20140520165838734?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvY2RsMjAwOHNreQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center "java.util.concurrent 的结构")

***

#### 线程池
##### 静态工厂
* newSingleThreadExecutor：创建一个单线程的线程池。这个线程池只有一个线程在工作，也就是相当于单线程串行执行所有任务。如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它。此线程池保证所有任务的执行顺序按照任务的提交顺序执行。

* newFixedThreadPool：创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。

* newCachedThreadPool：创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。

* newScheduledThreadPool：创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求。

* newSingleThreadScheduledExecutor：创建一个单线程的线程池。此线程池支持定时以及周期性执行任务的需求。

##### 线程池Executor任务拒绝策略
java.util.concurrent.RejectedExecutionHandler描述的任务操作。

* 第一种方式直接丢弃（DiscardPolicy）
* 第二种丢弃最旧任务（DiscardOldestPolicy）
* 第三种直接抛出异常（AbortPolicy）
* 第四种任务将有调用者线程去执行(CallerRunsPolicy)
