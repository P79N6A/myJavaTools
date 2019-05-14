package com.commons.javacode.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentRequestTest {

    public static void main(String[] args) {

        final AtomicInteger i = new AtomicInteger();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(">>>"+ConcurrentRequest.getRunningThreadNum());
                }
            }
        }).start();

        List<String> params = new ArrayList<String>();
        for(int j=0; j<8; j++){
            params.add("a");
        }

        ConcurrentRequest<String, Integer> cr = new ConcurrentRequest<String, Integer>(3, params) {
            @Override
            public Integer process(String param) throws IOException {
                int a = i.incrementAndGet();
                System.out.println(a);
                System.out.println("-----");
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(a==6){
                    throw new IOException("aaaaa");
                }
                return a;
            }
        };
        cr.setLogFlag("my log");
        Map<Integer, Integer> rs = cr.request();


        System.out.println("======" + cr.isSuccess());

        for(Map.Entry entry : rs.entrySet()){
            System.out.println(entry);
        }
    }


}
