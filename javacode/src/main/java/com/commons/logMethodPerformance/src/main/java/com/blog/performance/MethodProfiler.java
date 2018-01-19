package com.commons.logMethodPerformance.src.main.java.com.blog.performance;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.InitializingBean;

/**
 */
public class MethodProfiler implements InitializingBean {
    private Log log = LogFactory.getLog(MethodProfiler.class);
    private Map<Method, ProfilingFactor> factors = new ConcurrentHashMap<Method, ProfilingFactor>();
    
    private int threadCount = 1;
    private ThreadPoolExecutor threadPool;
    
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void afterPropertiesSet() throws Exception {
        this.threadPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(this.threadCount);
    }
    
    public Object doProfiling(ProceedingJoinPoint pjp) throws Throwable {
        long s = System.currentTimeMillis();
        Object object = pjp.proceed();
        long e = System.currentTimeMillis();
        Method method = new Method(pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
        long executionTime = e - s;
        threadPool.execute(new ProfilingThread(method, executionTime));
        return object;
    }
    
    public void doLogging() {
        Iterator<Map.Entry<Method, ProfilingFactor>> entries = factors.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Method, ProfilingFactor> entry = entries.next();
            log.info(entry.getKey().toString() + entry.getValue().toString());
        }
        factors.clear();
    }
    
    private class ProfilingThread extends Thread {
        private Method method;
        private long executionTime;
        
        public ProfilingThread(Method method, long executionTime) {
            this.method = method;
            this.executionTime = executionTime;
        }
        
        @Override
        public void run() {
            ProfilingFactor factor = factors.get(method);
            if (factor == null) {
                factor = new ProfilingFactor(1L, executionTime);
                factors.put(method, factor);
            }
            else {
                factor.addExecutionCount();
                factor.addExecutionTime(executionTime);
            }
        }
    }
}
