
#### 注入映射器

MapperFactoryBean 创建的代理类实现了 UserMapper 接口,并且注入到应用程序中。

    <bean id="userMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
      <property name="mapperInterface" value="com.mmz.mybatis.dao.UserDAO" />
      <property name="sqlSessionFactory" ref="sqlSessionFactory" />
    </bean>

没有必要在 Spring 的 XML 配置文件中注册所有的映射器。相反可以使用一个 MapperScannerConfigurer, 它将会查找类路径下的映射器并自动将它们创建成MapperFactoryBean

    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
      <property name="basePackage" value="com.mmz.mybatis.dao" />
      <!--<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />-->
      <!--<property name="annotationClass" value="com.mmz.mybatis.annotation.DataSourceDefault" />-->
    </bean>


#### spring的annotation-driven事务
    <bean id="txManagerA" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
        <property name="dataSource" ref="dataSource" />  
    </bean>  
    <tx:annotation-driven transaction-manager="txManagerA" />

    类或方法上加注解 @Transactional(value = "txManagerA")