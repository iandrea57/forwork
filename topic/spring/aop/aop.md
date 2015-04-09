
##### 代理模式
下面是代理模式的类图：

![](http://www.uml.org.cn/j2ee/images/2013011026.png)

代理模式中，存在一个称为ProxyObject的代理对象和RealObject的真实对象，它们都实现了相同的接口；

在调用的地方持有ProxyObject的实例，当调用request()方法时，ProxyObject可以在执行RealObject.request()前后做一些特定的业务，甚至不调用RealObject.request()方法。

##### JDK动态代理

    public static class ProxyFactory implements InvocationHandler {

        private Object delegate;

        public Object bind(Object delegate) {
            this.delegate = delegate;
            return Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
                    delegate.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("start method: " + method.getName());
            Object result = null;
            try {
                result = method.invoke(delegate, args);
            } catch (Exception e) {}
            System.out.println("end method: " + method.getName());
            System.out.println();
            return result;
        }
    }

    public static void main(String[] args) {
        IOffer offer = (IOffer) (new ProxyFactory().bind(new OfferImpl()));
        offer.postOffer();
        offer.modifyOffer();
    }

##### CGLIB动态代理

    public static class CglibProxyFactory implements MethodInterceptor {

        private Object delegate;

        public Object bind(Object delegate) {
            this.delegate = delegate;

            Enhancer hancer = new Enhancer();
            // 设置父类
            hancer.setSuperclass(delegate.getClass());
            // 回调
            hancer.setCallback(this);
            return hancer.create();
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                throws Throwable {
            System.out.println("start method: " + method.getName());
            Object result = null;
            try {
                result = proxy.invoke(delegate, args);
            } catch (Exception e) {}
            System.out.println("end method: " + method.getName());
            System.out.println();
            return result;
        }
    }

    public static void main(String[] args) {
        OfferImpl offer = (OfferImpl) (new CglibProxyFactory().bind(new OfferImpl()));
        offer.postOffer();
        offer.modifyOffer();
    }
    
#### Spring AOP的实现

##### Spring AOP的几个概念

1. 切面(Aspect)：切面就是一个关注点的模块化，如事务管理、日志管理、权限管理等；

2. 连接点(Joinpoint)：程序执行时的某个特定的点，在Spring中就是一个方法的执行；

3. 通知(Advice)：通知就是在切面的某个连接点上执行的操作，也就是事务管理、日志管理等；

4. 切入点(Pointcut)：切入点就是描述某一类选定的连接点，也就是指定某一类要织入通知的方法；

5. 目标对象(Target)：就是被AOP动态代理的目标对象；

用一张图来形象地表达AOP的概念及其关系如下：

![](http://www.uml.org.cn/j2ee/images/20130110211.png)

##### Spring AOP中切入点、通知、切面的实现

1. 切入点(Pointcut)：它定义了哪些连接点需要被织入横切逻辑；在Java中，连接点对应哪些类(接口)的方法。因此，我们都能猜到，所谓的切入点，就是定义了匹配哪些娄的哪些方法的一些规则，可以是静态的基于类(方法)名的值匹配，也可以是基于正则表达式的模式匹配。来看看Spring AOP Pointcut相关的类图：

    ![](http://www.uml.org.cn/j2ee/images/20130110212.png)

    在Pointcut接口的定义中，也许你已经想到了，ClassFilter是类过滤器，它定义了哪些类名需要拦截；典型的两个实现类为TypePatternClassFilter和TrueClassFilter(所有类均匹配)；而MethodMatcher为方法匹配器，定义哪些方法需要拦截。

    在上面的类图中：

    StaticMethodMatch与DynamicMethodMatch的区别是后者在运行时会依据方法的参数值进行匹配。
    
    NameMatchMethodPointCut根据指定的mappedNames来匹配方法。
    
    AbstractRegexpMethodPointCut根据正则表达式来匹配方法。
    
2. 通知(Advice)：通知定义了具体的横切逻辑。在Spring中，存在两种类型的Advice，即per-class和per-instance的Advice。
    
    所谓per-class，即该类型的Advice只提供方法拦截，不会为目标对象保存任何状态或者添加新的特性，它也是我们最常见的Advice。下面是per-class的类图：
    
    ![](http://www.uml.org.cn/j2ee/images/20130110213.png)
    
    BeforeAdvice：在连接点前执行的横切逻辑。
    
    AfterReturningAdvice：在连接点执行后，再执行横切逻辑。
    
    AfterAdvice：一般由程序自己实现，当抛出异常后，执行横切逻辑。
    
    AroundAdvice：Spring AOP中并没有提供这个接口，而是采用了AOP Alliance的MethodInteceptor接口；通过看AfterReturningAdvice的源码我们知道，它是不能更改连接点所在方法的返回值的(更改引用)；但使用的MethodInteceptor，所有的事情，都不在话下。
    
    在上面的类图中，还有两种类没有介绍，那就是\*\*\*AdviceAdapter和\***AdviceInteceptor，我们以AfterReturningAdviceInterceptor为例来说明：
    
    ![](http://www.uml.org.cn/j2ee/images/20130110214.png)
    
    该类实现了MethodInterceptor和AfterAdvice接口，同时构造函数中还有一个AfterReturningAdvice实例的参数；这个类存在的作用是为了什么呢？对，没错，Spring AOP把所有的Advice都适配成了MethodInterceptor，统一的好处是方便后面横切逻辑的执行(参看下一节)，适配的工作即由\***AdviceAdapter完成；

    哈哈，Spring AOP的代码也不过如此嘛：所谓的AfterReturningAdvice，通过适配成MethodInterceptor后，其实就是在invoke方法中，先执行目标对象的方法，再执行的AfterReturningAdvice所定义的横切逻辑。你现在明白它为什么不能修改返回值的引用了吧？

    对于per-instance的Advice，目前只有一种实现，就是Introduction，使用的场景比较少。
    
3. 切面(Aspect)：在Spring中，Advisor就是切面；但与通常的Aspect不同的是，Advisor通常只有一个Pointcut和一个Advice，而Aspect则可以包含多个Pointcut和多个Advice，因此Advisor是一种特殊的Aspect。但，这已经够用了！

    接下来看下per-class Advisor的类图：
    
    ![](http://www.uml.org.cn/j2ee/images/20130110215.png)
    
    其实没有什么好看的，前面已经说过，Advisor包含一个Pointcut和一个Advisor；在AbstractGenericPointcutAdvisor中，持有一个Advice的引用；下面的几个实现，均是针对前面提到的几种不同的Pointcut的实现。
    


 
   
