
#### 锁（lock）的代价
内核态的锁的时候需要操作系统进行一次上下文切换，加锁、释放锁会导致比较多的上下文切换和调度延时，等待锁的线程会被挂起直至锁释放。在上下文切换的时候，cpu之前缓存的指令和数据都将失效，对性能有很大的损失。

synchronized关键字保证同步的，这种通过使用一致的锁定协议来协调对共享状态的访问，可以确保无论哪个线程持有守护变量的锁，都采用独占的方式来访问这些变量，如果出现多个线程同时访问锁，那第一些线线程将被挂起，当线程恢复执行时，必须等待其它线程执行完他们的时间片以后才能被调度执行，在挂起和恢复执行过程中存在着很大的开销。

#### 乐观锁与悲观锁
独占锁是一种悲观锁，synchronized就是一种独占锁

乐观锁就是，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。

#### java中的原子操作

* all assignments of primitive types except for long and double
* all assignments of references
* all operations of java.concurrent.Atomic* classes
* all assignments to volatile longs and doubles

#### CAS
CAS的语义是“我认为V的值应该为A，如果是，那么将V的值更新为B，否则不修改并告诉V的值实际为多少”。

CAS是项乐观锁技术，当多个线程尝试使用CAS同时更新同一个变量时，只有其中一个线程能更新变量的值，而其它线程都失败，失败的线程并不会被挂起，而是被告知这次竞争中失败，并可以再次尝试。

CAS有3个操作数，内存值V，旧的预期值A，要修改的新值B。当且仅当预期值A和内存值V相同时，将内存值V修改为B，否则什么都不做。

在高度争用的情况下（即有多个线程不断争用一个内存位置的时候），基于锁的算法开始提供比非阻塞算法更好的吞吐率，因为当线程阻塞时，它就会停止争用，耐心地等候轮到自己，从而避免了进一步争用。但是，这么高的争用程度并不常见，因为多数时候，线程会把线程本地的计算与争用共享数据的操作分开，从而给其他线程使用共享数据的机会。

#### volatile
如果一个变量加了volatile关键字，就会告诉编译器和JVM的内存模型：这个变量是对所有线程共享的、可见的，每次jvm都会读取最新写入的值并使其最新值在所有CPU可见。

volatile变量具有“lock”的可见性，却不具备原子特性。

两个准则，只要我们的程序能遵循它，就可以放心使用volatile变量来实现线程安全。

1. 对变量的写操作不依赖于当前值。
2. 该变量没有包含在具有其他变量的不变式中。

只有在其状态完全独立于程序其他状态时才可使用volatile变量。

##### volatile没有原子性举例：AtomicInteger自增
例如你让一个volatile的integer自增（i++），其实要分成3步：1）读取volatile变量值到local； 2）增加变量的值；3）把local的值写回，让其它的线程可见。这3步的jvm指令为：

    mov    0xc(%r10),%r8d ; Load
    inc    %r8d           ; Increment
    mov    %r8d,0xc(%r10) ; Store
    lock addl $0x0,(%rsp) ; StoreLoad Barrier

注意最后一步是内存屏障。

内存屏障（memory barrier）是一个CPU指令。编译器和CPU可以在保证输出结果一样的情况下对指令重排序，使性能得到优化。插入一个内存屏障，相当于告诉CPU和编译器先于这个命令的必须先执行，后于这个命令的必须后执行。内存屏障另一个作用是强制更新一次不同CPU的缓存。例如，一个写屏障会把这个屏障前写入的数据刷新到缓存，这样任何试图读取该数据的线程将得到最新值，而不用考虑到底是被哪个cpu核心或者哪颗CPU执行的。

从Load到store到内存屏障，一共4步，其中最后一步jvm让这个最新的变量的值在所有线程可见，也就是最后一步让所有的CPU内核都获得了最新的值，但**中间的几步（从Load到Store）**是不安全的，中间如果其他的CPU修改了值将会丢失。

#### AtomicXXX具有原子性和可见性
其实AtomicLong的源码里也用到了volatile，但只是用来读取或写入，见源码：

    public class AtomicLong extends Number implements java.io.Serializable {
        private volatile long value;
     
        /**
         * Creates a new AtomicLong with the given initial value.
         *
         * @param initialValue the initial value
         */
        public AtomicLong(long initialValue) {
            value = initialValue;
        }
     
        /**
         * Creates a new AtomicLong with initial value {@code 0}.
         */
        public AtomicLong() {
        }

其CAS源码核心代码为：

    int compare_and_swap (int* reg, int oldval, int newval) 
    {
      ATOMIC();
      int old_reg_val = *reg;
      if (old_reg_val == oldval) 
         *reg = newval;
      END_ATOMIC();
      return old_reg_val;
    }

虚拟机指令为：

    mov    0xc(%r11),%eax       ; Load
    mov    %eax,%r8d            
    inc    %r8d                 ; Increment
    lock cmpxchg %r8d,0xc(%r11) ; Compare and exchange

因为CAS是基于乐观锁的，也就是说当写入的时候，如果寄存器旧值已经不等于现值，说明有其他CPU在修改，那就继续尝试。所以这就保证了操作的原子性。

![](http://images.cnitblog.com/blog/28306/201402/191824497484728.png)

##### AtomicXXX与ConcurrentXXX

    public final long addAndGet(long delta) {
        for (;;) {
            long current = get();
            long next = current + delta;
            if (compareAndSet(current, next))
                return next;
        }
    }
    public final boolean compareAndSet(long expect, long update) {
        return unsafe.compareAndSwapLong(this, valueOffset, expect, update);
    }

concurrentStack

    public class ConcurrentStack<E> {
        AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();
        public void push(E item) {
            Node<E> newHead = new Node<E>(item);
            Node<E> oldHead;
            do {
                oldHead = head.get();
                newHead.next = oldHead;
            } while (!head.compareAndSet(oldHead, newHead));
        }
        public E pop() {
            Node<E> oldHead;
            Node<E> newHead;
            do {
                oldHead = head.get();
                if (oldHead == null) 
                    return null;
                newHead = oldHead.next;
            } while (!head.compareAndSet(oldHead,newHead));
            return oldHead.item;
        }
        static class Node<E> {
            final E item;
            Node<E> next;
            public Node(E item) { this.item = item; }
        }
    }

非阻塞链表

    public class LinkedQueue <E> {
        private static class Node <E> {
            final E item;
            final AtomicReference<Node<E>> next;
            Node(E item, Node<E> next) {
                this.item = item;
                this.next = new AtomicReference<Node<E>>(next);
            }
        }
        private AtomicReference<Node<E>> head
            = new AtomicReference<Node<E>>(new Node<E>(null, null));
        private AtomicReference<Node<E>> tail = head;
        public boolean put(E item) {
            Node<E> newNode = new Node<E>(item, null);
            while (true) {
                Node<E> curTail = tail.get();
                Node<E> residue = curTail.next.get();
                if (curTail == tail.get()) {
                    if (residue == null) /* A */ {
                        if (curTail.next.compareAndSet(null, newNode)) /* C */ {
                            tail.compareAndSet(curTail, newNode) /* D */ ;
                            return true;
                        }
                    } else {
                        tail.compareAndSet(curTail, residue) /* B */;
                    }
                }
            }
        }
    }

ConcurrentHashMap的实现原理

用桶粒度的锁，避免了put和get中对整个map的锁定，尤其在get中，只对一个HashEntry做锁定操作，性能提升是显而易见的。

![](http://images.cnitblog.com/blog/28306/201402/121727197879329.png)