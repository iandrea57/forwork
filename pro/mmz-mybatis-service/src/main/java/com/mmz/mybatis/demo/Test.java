package com.mmz.mybatis.demo;

import com.mmz.mybatis.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dra on 15-5-5.
 */
@Component
public class Test {

    @Autowired
    private UserDAO userMapper;

    public void test() {
//        System.out.println(userMapper.getAll());
        System.out.println(userMapper.getAllNames());
    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Test test = (Test) context.getBean("test", Test.class);
        test.test();
    }
}
