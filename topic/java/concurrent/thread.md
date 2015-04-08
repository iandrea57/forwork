#### uncaught exception
Thread的run方法是不抛出任何检查型异常(checked exception)的,但是它自身却可能因为一个异常而被终止，导致这个线程的终结。

在JDK5.0中，我们通过Thread的实例方法setUncaughtExceptionHandler，可以为任何一个Thread设置一个UncaughtExceptionHandler。

#### setDaemon(true)
Java将线程分为User线程和Daemon线程两种。通常Daemon线程用来为User线程提供某些服务。

程序的main()方法线程是一个User进程。User进程创建的进程为User进程。当所有的User线程结束后，JVM才会结束。

daemon的作用。当设置线程t为Daemon线程时，只要User线程（main线程）一结束，JVM也就退出了，deamon线程结束

需要注意的是，setDaemon()方法必须在调用线程的start()方法之前调用。
