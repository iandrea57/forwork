
##### 代理模式
下面是代理模式的类图：

![](http://www.uml.org.cn/j2ee/images/2013011026.png)

代理模式中，存在一个称为ProxyObject的代理对象和RealObject的真实对象，它们都实现了相同的接口；

在调用的地方持有ProxyObject的实例，当调用request()方法时，ProxyObject可以在执行RealObject.request()前后做一些特定的业务，甚至不调用RealObject.request()方法。

##### JDK动态代理

