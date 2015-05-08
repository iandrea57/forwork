/**
 * $Id$
 * Copyright 2011-2013 Oak Pacific Interactive. All rights reserved.
 */
package com.mmz.mybatis.service;

import com.mmz.mybatis.dao.AgentDAO;
import com.mmz.mybatis.dao.UserDAO;
import com.mmz.mybatis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * @author <a href="mailto:hailong.peng@renren-inc.com">彭海龙</a>
 * @createTime 15-5-6 下午4:44
 */
@Service
public class TestService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AgentDAO agentDAO;
    int threshold = -1;

    private User user1 = new User("User1");
    private User user2 = new User("User2");

    public void test() {
        for (User u : userDAO.getAll()) {
            System.out.println(u);
        }
    }

    public void preTransactional() {
        userDAO.delete(user1);
        userDAO.delete(user2);
    }

    public void afterTransactionl(int i) {
        if (userDAO.getCouont(user1) == userDAO.getCouont(user2)) {
            System.out.println("-------------" + i + "-- valid ------------------");
        } else {
            System.out.println("------------" + i + "-- not valid -----------------");
        }
    }

    @Transactional("txManagerLbs")
    public void testTransactional() {

        userDAO.insert(user1);
        int i = new Random().nextInt(20);
        if (i > threshold) {
            System.out.println("error");
            throw new IllegalStateException("interrupt Transactional");
        } else {
            System.out.println("success then do another db change. ");
        }

        userDAO.insert(user2);
        System.out.println("finish change ");
    }

    public void testNoTransactional() {

        userDAO.insert(user1);
        int i = new Random().nextInt(20);
        if (i > threshold) {
            System.out.println("error");
            throw new IllegalStateException("interrupt Transactional");
        } else {
            System.out.println("success then do another db change. ");
        }

        userDAO.insert(user2);
        System.out.println("finish change ");
    }

    @Transactional("txManagerDefault")
    public void testOtherTransactional() {

        userDAO.insert(user1);
        userDAO.insert(user2);
        int i = new Random().nextInt(20);
        if (i > threshold) {
            System.out.println("error");
            throw new IllegalStateException("interrupt Transactional");
        } else {
            System.out.println("success then do another db change. ");
        }

        userDAO.insert(user2);
        System.out.println("finish change ");
    }

    public void insert() {
        int id = userDAO.insert(user1);
        System.out.println("id = " + id);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TestService test = context.getBean("testService", TestService.class);
        test.insert();
        System.exit(0);
    }
}
