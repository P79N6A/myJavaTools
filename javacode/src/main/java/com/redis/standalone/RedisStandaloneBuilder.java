package com.redis.standalone;

import com.alibaba.fastjson.JSONObject;


import com.commons.http.ConstUtils;
import com.commons.http.HttpUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;
import redis.clients.util.AuthUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class RedisStandaloneBuilder {
    private Logger logger = LoggerFactory.getLogger(RedisStandaloneBuilder.class);

    private static final Lock LOCK = new ReentrantLock();
    private volatile JedisPool jedisPool;
    private GenericObjectPoolConfig poolConfig;
    private final long appId;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    /**
     * 是否开启统计
     */
    private boolean clientStatIsOpen = true;

    /**
     * 是否开启统计,默认开启
     */
    private boolean exceptionStatIsOpen = true;

    /**
     * 是否开启耗时和值大小统计,默认关闭
     */
    private boolean costAndValueStatsIsOpen = false;

    /**
     * 构造函数package访问域，package外直接构造实例；
     *
     * @param appId
     */
    RedisStandaloneBuilder(final long appId) {
        this.appId = appId;
        poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 3);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
        poolConfig.setJmxEnabled(true);
        poolConfig.setJmxNamePrefix("jedis-pool");
    }

    public JedisPool build() {
        if (jedisPool == null) {
            while (true) {
                try {
                    LOCK.tryLock(100, TimeUnit.MILLISECONDS);
                    if (jedisPool == null) {
                        /**
                         * 心跳返回的请求为空；
                         */
                        String response = HttpUtils.doGet(String.format(ConstUtils.REDIS_STANDALONE_URL, appId));
                        if (response == null || response.isEmpty()) {
                            logger.warn("cannot get response from server, appId={}. continue...", appId);
                            continue;
                        }
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = JSONObject.parseObject(response);
                        } catch (Exception e) {
                            logger.error("read json from response error, appId: {}.", appId, e);
                        }
                        if (jsonObject == null) {
                            logger.warn("invalid response, appId: {}. continue...", appId);
                            continue;
                        }
                        /**
                         * 从心跳中提取HostAndPort，构造JedisPool实例；
                         */
                        String instance = jsonObject.getString("standalone");
                        String[] instanceArr = instance.split(":");
                        if (instanceArr.length != 2) {
                            logger.warn("instance info is invalid, instance: {}, appId: {}, continue...", instance,
                                    appId);
                            continue;
                        }

                        //收集上报数据
                        if (clientStatIsOpen) {
                            ClientDataCollectReportExecutor.getInstance();
                        }

                        UsefulDataCollector.setExceptionStats(exceptionStatIsOpen);
                        UsefulDataCollector.setCostAndValueStats(costAndValueStatsIsOpen);

                        String pkey = jsonObject.getString("pkey");
                        if (pkey != null && pkey.length() > 0) {
                            pkey = AuthUtil.getAppIdMD5(jsonObject.getString("pkey"));
                        } else {
                            pkey = "";
                        }
                        String password = appId + AuthUtil.SPLIT_KEY + pkey;

                        jedisPool = new JedisPool(poolConfig, instanceArr[0], Integer.valueOf(instanceArr[1]),
                                timeout, password);
                        return jedisPool;
                    }
                } catch (InterruptedException e) {
                    logger.error("error in build().", e);
                }
            }
        }
        return jedisPool;
    }

    /**
     * 配置
     *
     * @param poolConfig
     * @return
     */
    public RedisStandaloneBuilder setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    /**
     * @param timeout 单位:毫秒
     * @return
     */
    public RedisStandaloneBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 是否开启统计
     *
     * @param clientStatIsOpen
     * @return
     */
    public RedisStandaloneBuilder setClientStatIsOpen(boolean clientStatIsOpen) {
        this.clientStatIsOpen = clientStatIsOpen;
        return this;
    }

    /**
     * 是否开启异常统计
     *
     * @param clientStatIsOpen
     * @return
     */
    public RedisStandaloneBuilder setExcetpionStatIsOpen(boolean clientStatIsOpen) {
        this.clientStatIsOpen = clientStatIsOpen;
        return this;
    }

    /**
     * 是否开启耗时和值大小统计
     *
     * @param costAndValueStatsIsOpen
     * @return
     */
    public RedisStandaloneBuilder setCostAndValueStatsIsOpen(boolean costAndValueStatsIsOpen) {
        this.costAndValueStatsIsOpen = costAndValueStatsIsOpen;
        return this;
    }
}
