package com.commons.javacode.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;


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

    private ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new RequestThreadFactory(), new ThreadPoolExecutor.AbortPolicy());;

    private List<T> params = new ArrayList<T>();

    private Results results;
    private static AtomicInteger runningThreadNum = new AtomicInteger();
    private String logFlag;


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
     * @param params 所有请求参数
     */
    public ConcurrentRequest(int conNum, List<T> params) throws IllegalArgumentException {
        this.conNum = conNum;
        this.totalNum = params.size();  //入参个数应该与总请求数相等
        semaphore = new Semaphore(conNum);
        countDownLatch = new CountDownLatch(totalNum);
        this.params = params;

    }

    /**
     * 异步调用的方法无参
     * @param conNum 最大并发数
     * @param maxConNum 超过3页，并发量是maxConNum
     * @param totalNum 总的请求数
     */
    public ConcurrentRequest(int conNum, int maxConNum, int totalNum) throws IllegalArgumentException {
        this.conNum = conNum;
        this.totalNum = totalNum;
        if(totalNum/(float)conNum > 3){
            this.conNum = maxConNum;
        }
        semaphore = new Semaphore(conNum);
        countDownLatch = new CountDownLatch(totalNum);
    }

    /**
     * 异步调用的方法有参
     * @param conNum 最大并发数
     * @param maxConNum 超过3页，并发量是maxConNum
     * @param params 所有请求参数
     */
    public ConcurrentRequest(int conNum, int maxConNum, List<T> params) throws IllegalArgumentException {
        this.conNum = conNum;
        this.totalNum = params.size();  //入参个数应该与总请求数相等
        if(this.totalNum/(float)conNum > 3){
            this.conNum = maxConNum;
        }
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
    public Results request(){
        long st = System.currentTimeMillis();

        final Map<Integer, V> validResult = new ConcurrentHashMap<Integer, V>();
        final Map<Integer, NullResult> nullResult = new ConcurrentHashMap<Integer, NullResult>();
        final Map<Integer, ExceptionResult> exceptionResult = new ConcurrentHashMap<Integer, ExceptionResult>();

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
                        runningThreadNum.incrementAndGet();

                        V rs = process(param);
                        if(rs != null) {
                            validResult.put(num, rs);
                        }else{
                            nullResult.put(num, new NullResult());
                        }
                    }catch (Throwable t){
                        exceptionResult.put(num, new ExceptionResult());
                        log.error("ConcurrentRequest " + logFlag + ",totalNum:" + totalNum + ",conNum:" + conNum + ",i:" + num, t);
                    }finally {
                        semaphore.release();
                        countDownLatch.countDown();
                        runningThreadNum.decrementAndGet();
                    }

                }
            });
        }

        try {
            countDownLatch.await();
            this.results = new Results(validResult, nullResult, exceptionResult);
            long cost = System.currentTimeMillis() - st;
            log.info("ConcurrentRequest " + logFlag + ",totalNum:" + totalNum + ",conNum:" + conNum +
                    ",successNum:" + this.results.getSuccessNum() + ",isAllSuccess:" + isSuccess() + ",cost:" + cost + ",runningThreadNum:" + getRunningThreadNum());
            return results;
        } catch (InterruptedException t) {
            log.error("ConcurrentRequest " + logFlag + ",totalNum:" + totalNum + ",conNum:" + conNum, t);
        } finally {
            pool.shutdown();
        }
        return null;
    }

    public abstract V process(T param) throws Exception;


    /**
     * @return 只有全部任务成功，才是true
     */
    public boolean isSuccess(){
        if(results == null) return false;
        return results.getSuccessNum() == totalNum;
    }

    public static int getRunningThreadNum(){
        return runningThreadNum.intValue();
    }

    public void setLogFlag(String log){
        this.logFlag = log;
    }

    /**
     * 当异步方法没有返回值时，可以返回该类型
     */
    class VoidResult{

    }

    class NullResult{

    }

    class ExceptionResult{

    }

    public class Results{
        private Map<Integer, V> validResult = new ConcurrentHashMap<Integer, V>();
        private Map<Integer, NullResult> nullResult = new ConcurrentHashMap<Integer, NullResult>();
        private Map<Integer, ExceptionResult> exceptionResult = new ConcurrentHashMap<Integer, ExceptionResult>();

        public Results() {
        }

        public Results(Map<Integer, V> validResult, Map<Integer, NullResult> nullResult, Map<Integer, ExceptionResult> exceptionResult) {
            this.validResult = validResult;
            this.nullResult = nullResult;
            this.exceptionResult = exceptionResult;
        }

        /**
         * map中的key可能不是连续的，中间可能有返回为null或调用失败的导致不连续
         */
        public Map<Integer, V> getValidResult() {
            return validResult;
        }

        public void setValidResult(Map<Integer, V> validResult) {
            this.validResult = validResult;
        }

        public Map<Integer, NullResult> getNullResult() {
            return nullResult;
        }

        public void setNullResult(Map<Integer, NullResult> nullResult) {
            this.nullResult = nullResult;
        }

        public Map<Integer, ExceptionResult> getExceptionResult() {
            return exceptionResult;
        }

        public void setExceptionResult(Map<Integer, ExceptionResult> exceptionResult) {
            this.exceptionResult = exceptionResult;
        }

        public List<V> getValidResultList(){
            if(this.validResult != null){
                List<V> list = new ArrayList<V>();

                List<Map.Entry<Integer, V>> entryList = new ArrayList<Map.Entry<Integer, V>>(this.validResult.entrySet());
                Collections.sort(entryList,new Comparator<Map.Entry<Integer, V>>() {
                    //升序排序
                    public int compare(Map.Entry<Integer, V> o1,
                                       Map.Entry<Integer, V> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }

                });
                for(Map.Entry<Integer, V> entry:entryList){
                    list.add(entry.getValue());
                }

                return list;
            }
            return null;
        }

        public int getSuccessNum(){
            int n = 0;
            if(validResult != null){
                n += validResult.size();
            }
            if(nullResult != null){
                n += nullResult.size();
            }
            return n;
        }

        public int getFailNum(){
            if(exceptionResult == null) return 0;
            return exceptionResult.size();
        }
    }

    static class RequestThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        RequestThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "ConcurrentRequestPool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
