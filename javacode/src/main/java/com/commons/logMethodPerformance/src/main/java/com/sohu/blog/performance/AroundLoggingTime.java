package com.sohu.blog.performance;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
/**
 * @author 金海民
 */
public class AroundLoggingTime {

    private static final Log log = LogFactory.getLog(AroundLoggingTime.class);
    //private static final ProfilerManager profilerManager = ProfilerManager.getLocal();    

    public Object doAroundLoggingTime(ProceedingJoinPoint pjp) throws Throwable {
    	/*
    	String name = pjp.getTarget().getClass().toString();
    	ProfilerPoint profilerPoint = profilerManager.getProfilerPoint(name);
    	log.debug(profilerPoint.toString());
    	Profiler profiler = profilerPoint.start();    	
        Object retVal = pjp.proceed();
    	profiler.finish();
        return retVal;
    	*/
    	
    	
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        
        int size = 0;
        if (retVal instanceof Collection) {
            size = ((Collection)retVal).size();
        }
        long spendTime = System.currentTimeMillis() - time;
        //耗时超过50ms打印参数
        if (spendTime > 50) {
            StringBuffer sb = new StringBuffer();
            Object[] args = pjp.getArgs();
            for (int i = 0, length = args.length ; i < length; i++) {
                sb.append(" ").append("p").append(i).append(":").append(args[i]);
            }
            log.info(pjp.getSignature().getDeclaringTypeName() + " " + pjp.toShortString() + " process time: "
                    + (spendTime) + " size: " + size + " params: " + sb.toString());
        } else {
            log.debug(pjp.getSignature().getDeclaringTypeName() + " " + pjp.toShortString() + " process time: "
                    + (spendTime) + " size: " + size);
        }
        return retVal;
    }
}
