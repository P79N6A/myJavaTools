/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.commons.hystrix.requestWrapper;

import com.netflix.hystrix.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Description: 外部接口容灾使用
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2018年6月8日
 */
public abstract class SafeServiceRequest extends HystrixCommand<String> {
    private static final Log log = LogFactory.getLog(SafeServiceRequest.class);

    public SafeServiceRequest(String commandName, String groupName, String poolName) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupName))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandName))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(poolName))

                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        .withExecutionIsolationThreadTimeoutInMilliseconds(3000))

            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                    .withMaxQueueSize(10)
                    .withCoreSize(30)
                    .withQueueSizeRejectionThreshold(7))

            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withCircuitBreakerEnabled(true)
                    .withCircuitBreakerErrorThresholdPercentage(50)
                    .withCircuitBreakerRequestVolumeThreshold(3)
                    .withCircuitBreakerSleepWindowInMilliseconds(60 * 1000 * 1)));

    }

    @Override
    protected String getFallback() {
        log.error("fallback,timeout:" + isResponseTimedOut() + ",circuitBreakerOpen:" + isCircuitBreakerOpen() + ",reject:"
                + isResponseRejected());
        return null;
    }

}

