package com.mmz.mybatis.demo;

import com.mmz.mybatis.dao.AgentDAO;
import com.mmz.mybatis.dao.UserDAO;
import com.mmz.mybatis.service.TestService;
import com.xiaonei.xce.dbpool.XceDataSource;
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
    private TestService testService;

    public void test(int i) {
//        testService.preTransactional();
        i = i % 3;
        try {
            switch (i) {
                case 0:
                    testService.testTransactional();
                    break;
                case 1:
                    testService.testNoTransactional();
                    break;
                case 2:
                    testService.testOtherTransactional();
                    break;
            }
        } catch (IllegalStateException e) {

        }
//        testService.afterTransactionl(i);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Test test = context.getBean("test", Test.class);
        for (int i = 0; i < 3; i++) {
            test.test(i);
        }
//        test.test(2);
        System.exit(0);
    }
}
