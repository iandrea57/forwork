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
![concurrent结构](http://img.blog.csdn.net/20140520165838734?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvY2RsMjAwOHNreQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center "java.util.concurrent 的结构")

***
