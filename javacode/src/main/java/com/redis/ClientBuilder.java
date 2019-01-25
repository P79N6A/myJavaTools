package com.redis;

import com.redis.cluster.RedisClusterBuilder;
import com.redis.sentinel.RedisSentinelBuilder;
import com.redis.standalone.RedisStandaloneBuilder;

public class ClientBuilder {

    /**
     * 构造redis cluster的builder
     *
     * @param appId
     * @return
     */
    public static RedisClusterBuilder redisCluster(final long appId) {
        return new RedisClusterBuilder(appId);
    }

    /**
     * 构造redis sentinel的builder
     *
     * @param appId
     * @return
     */
    public static RedisSentinelBuilder redisSentinel(final long appId) {
        return new RedisSentinelBuilder(appId);
    }

    /**
     * 构造redis standalone的builder
     * @param appId
     * @return
     */
    public static RedisStandaloneBuilder redisStandalone(final long appId) {
        return new RedisStandaloneBuilder(appId);
    }
}
