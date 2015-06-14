package com.mmz.forwork.java.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dra on 15-6-6.
 */
public class Test {

    public static void main(String args[]) {

        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(4);
        new ThreadPoolExecutor(4, 10, 10, TimeUnit.SECONDS, queue);
    }
}
