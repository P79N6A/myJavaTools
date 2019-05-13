package com.commons.javacode.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * 控制并发数量，控制所有任务完成后才继续向下执行
 * @param T 异步调用的方法process入参类型
 * @param V 异步调用的方法process返回类型
 */
public abstract class ConcurrentRequest<T,V> {
    private Logger log = LoggerFactory.getLogger(ConcurrentRequest.class);

    private int conNum;
    private int totalNum;

    private CountDownLatch countDownLatch;
    private Semaphore semaphore;

    private ExecutorService pool = Executors.newCachedThreadPool();

    private List<T> params = new ArrayList<T>();

    private Map<Integer, V> result;

//    /**
//     * @param conNum 最大并发数
//     * @param totalNum 总的请求数
//     */
//    public ConcurrentRequest(int conNum, int totalNum, List params) throws IllegalArgumentException {
//        if(params != null){
//            if(!(totalNum == params.size())){
//                throw new IllegalArgumentException("totalNum need match params size");
//            }
//        }
//
//        this.conNum = conNum;
//        this.totalNum = totalNum;
//        semaphore = new Semaphore(conNum);
//        countDownLatch = new CountDownLatch(totalNum);
//        this.params = params;
//
//    }

    /**
     * 异步调用的方法无参
     * @param conNum 最大并发数
     * @param totalNum 总的请求数
     */
    public ConcurrentRequest(int conNum, int totalNum) throws IllegalArgumentException {
        this.conNum = conNum;
        this.totalNum = totalNum;
        semaphore = new Semaphore(conNum);
        countDownLatch = new CountDownLatch(totalNum);
    }

    /**
     * 异步调用的方法有参
     * @param conNum 最大并发数
     * @param totalNum 总的请求数
     */
    public ConcurrentRequest(int conNum, List<T> params) throws IllegalArgumentException {
        this.conNum = conNum;
        this.totalNum = params.size(); //入参个数应该与总请求数相等
        semaphore = new Semaphore(conNum);
        countDownLatch = new CountDownLatch(totalNum);
        this.params = params;

    }

    /**
     * @return 返回的Map中的Key的顺序和构造方法中参数的顺序一样，如List params中第2个参数，则返回值放到了map.get(2)中
     * 如果异步方法抛出异常，则返回的map中没有对应的kv。
     *
     * 注意：
     * 异步方法必须有返回值
     * 部分任务失败，其它任务会继续执行
     */
    public Map<Integer, V> request(){
        long st = System.currentTimeMillis();

        final Map<Integer, V> result = new ConcurrentHashMap<Integer, V>();

        for(int i=0; i<totalNum; i++){

            T p = null;
            if(params != null){
                p = params.get(i);
            }

            final T param = p;
            final int num=i;
            pool.execute(new Runnable(){

                @Override
                public void run() {
                    try{
                        semaphore.acquire();

                        V rs = process(param);

                        result.put(num, rs);

                    }catch (Throwable t){
                        log.error("totalNum:" + totalNum + ",conNum:" + conNum + ",i:" + num, t);
                    }finally {
                        semaphore.release();
                        countDownLatch.countDown();
                    }

                }
            });
        }

        try {
            countDownLatch.await();
            long cost = System.currentTimeMillis() - st;
            log.info("totalNum:" + totalNum + ",conNum:" + conNum + ",cost:" + cost);
            this.result = result;
            return result;
        } catch (InterruptedException t) {
            log.error("totalNum:" + totalNum + ",conNum:" + conNum, t);
        }
        return null;
    }

    public abstract V process(T param) throws Exception;


    /**
     * @return 只有全部任务成功，才是true
     */
    public boolean isSuccess(){
        if(result == null) return false;

        return result.size() == totalNum;
    }

}
