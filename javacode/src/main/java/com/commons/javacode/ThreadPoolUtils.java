/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package com.commons.javacode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Description: 
 * </p>
 */
public class ThreadPoolUtils {

    //方法一，spring bean方式
    /*
    <bean id="taskExecutor"
            class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
            <property name="corePoolSize" value="4" />
            <property name="maxPoolSize" value="10" />
        </bean>
        */
    public void test(){
        taskExecutor.execute(new Runnable(){

            public void run() {
                System.out.println("test");
            }
            
        });
    }


    //方法二：
    //同一时间只允许一个任务运行，当有任务运行时，新来的任务直接丢弃掉
    private static Executor pool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardOldestPolicy());



}

    