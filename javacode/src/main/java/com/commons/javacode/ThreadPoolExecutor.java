/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package com.commons.javacode;
/**
 * <p>
 * Description: 
 * </p>
 */
public class ThreadPoolExecutor {
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
    
}

    