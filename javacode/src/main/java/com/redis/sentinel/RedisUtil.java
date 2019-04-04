package com.redis.sentinel;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

import java.util.HashSet;
import java.util.Set;

public class RedisUtil {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private GenericObjectPoolConfig poolConfig;
    private JedisSentinelPool sentinelPool;

    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;


    RedisUtil(final long appId) {
        poolConfig = new JedisPoolConfig();
//      poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        poolConfig.setMaxWaitMillis(1000L);
//        poolConfig.setJmxNamePrefix("jedis-sentinel-pool");
//        poolConfig.setJmxEnabled(true);


    }

    public JedisSentinelPool build() {
        if (sentinelPool == null) {
            String masterName = "";
            Set<String> sentinelSet = new HashSet<String>();
            String password = null;
            sentinelPool = new JedisSentinelPool(masterName, sentinelSet, poolConfig, connectionTimeout,
                    soTimeout, password, Protocol.DEFAULT_DATABASE);
        }
        return sentinelPool;
    }

    public JedisSentinelPool getJedisSentinelPool() {
        return sentinelPool;
    }


}
