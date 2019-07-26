package com.commons.javacode.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadDemo {

    public static void main(String[] args){
        //最多只能有一个线程
        //方一
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        //方二
        //下面也可以是newFixedThreadPool，newCachedThreadPool
        ThreadPoolExecutor taskPool=(ThreadPoolExecutor)Executors.newSingleThreadExecutor();
        int threadCount = taskPool.getActiveCount();
        if(threadCount>0){
            System.out.println("last task isruning...");
            return;
        }
    }

}
