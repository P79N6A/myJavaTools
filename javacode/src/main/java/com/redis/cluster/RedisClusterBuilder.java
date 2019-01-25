package com.redis.cluster;

import com.commons.http.ConstUtils;
import com.commons.http.HttpUtils;
import com.sun.mail.iap.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.PipelineCluster;
import redis.clients.jedis.Protocol;
import redis.clients.util.AuthUtil;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisClusterBuilder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 应用id
     */
    private final long appId;
    /**
     * jedis对象池配置
     */
    private GenericObjectPoolConfig jedisPoolConfig;
    /**
     * jedis集群对象
     */
    private PipelineCluster pipelineCluster;

    /**
     * jedis连接超时(单位:毫秒)
     */
    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * jedis读写超时(单位:毫秒)
     */
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * 节点定位重试次数:默认3次
     */
    private int maxAttempts = 5;

    /**
     * 是否为每个JeidsPool初始化Jedis对象
     */
    private boolean whetherInitIdleJedis = false;

    /**
     * 构建锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 是否开启统计,默认开启
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
     * 构造函数package访问域，package外不能直接构造实例；
     *
     * @param appId
     */
    RedisClusterBuilder(final long appId) {
        this.appId = appId;
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 5);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
        //JedisPool.borrowObject最大等待时间
        poolConfig.setMaxWaitMillis(1000L);
        poolConfig.setJmxNamePrefix("jedis-pool");
        poolConfig.setJmxEnabled(true);
        this.jedisPoolConfig = poolConfig;
    }

    public PipelineCluster build() {
        if (pipelineCluster == null) {
            while (true) {
                try {
                    lock.tryLock(10, TimeUnit.SECONDS);
                    if (pipelineCluster != null) {
                        return pipelineCluster;
                    }
                    String url = String.format(ConstUtils.REDIS_CLUSTER_URL, String.valueOf(appId));
                    String response = HttpUtils.doGet(url);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = JSONObject.parseObject(response);
                    } catch (Exception e) {
                        logger.error("remote build error, appId: {}", appId, e);
                    }
                    if (jsonObject == null) {
                        logger.error("get cluster info for appId: {} error. continue...", appId);
                        continue;
                    }
                    int status = jsonObject.getIntValue("status");
                    String message = jsonObject.getString("message");

                    /** 检查客户端版本 **/
                    if (status == ClientStatusEnum.ERROR.getStatus()) {
                        throw new IllegalStateException(message);
                    } else if (status == ClientStatusEnum.WARN.getStatus()) {
                        logger.warn(message);
                    } else {
                        logger.info(message);
                    }

                    Set<HostAndPort> nodeList = new HashSet<HostAndPort>();

                    String nodeInfo = jsonObject.getString("shardInfo");
                    String[] pairArray = nodeInfo.split(" ");
                    for (String pair : pairArray) {
                        String[] nodes = pair.split(",");
                        for (String node : nodes) {
                            String[] ipAndPort = node.split(":");
                            if (ipAndPort.length < 2) {
                                continue;
                            }
                            String ip = ipAndPort[0];
                            int port = Integer.parseInt(ipAndPort[1]);
                            nodeList.add(new HostAndPort(ip, port));
                        }
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
                    pipelineCluster = new PipelineCluster(jedisPoolConfig, nodeList, connectionTimeout, soTimeout,
                            maxAttempts, password, whetherInitIdleJedis);
                    return pipelineCluster;
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    lock.unlock();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } else {
            return pipelineCluster;
        }
    }

    /**
     * 设置配置
     *
     * @param jedisPoolConfig
     * @return
     */
    public RedisClusterBuilder setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
        return this;
    }

    /**
     * 兼容老版本参数
     *
     * @param timeout
     * @return
     */
    public RedisClusterBuilder setTimeout(final int timeout) {
        this.connectionTimeout = compatibleTimeout(timeout);
        this.soTimeout = compatibleTimeout(timeout);
        return this;
    }

    /**
     * 设置jedis连接超时时间
     *
     * @param connectionTimeout
     */
    public RedisClusterBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = compatibleTimeout(connectionTimeout);
        return this;
    }

    /**
     * 设置jedis读写超时时间
     *
     * @param soTimeout
     */
    public RedisClusterBuilder setSoTimeout(int soTimeout) {
        this.soTimeout = compatibleTimeout(soTimeout);
        return this;
    }

    /**
     * 是否为每个JedisPool创建空闲Jedis
     *
     * @param whetherInitIdleJedis
     * @return
     */
    public RedisClusterBuilder setWhetherInitIdleJedis(boolean whetherInitIdleJedis) {
        this.whetherInitIdleJedis = whetherInitIdleJedis;
        return this;
    }

    /**
     * redis操作超时时间:默认2秒
     * 如果timeout小于0 超时:200微秒
     * 如果timeout小于100 超时:timeout*10000微秒
     * 如果timeout大于100 超时:timeout微秒
     */
    private int compatibleTimeout(int paramTimeOut) {
        if (paramTimeOut <= 0) {
            return Protocol.DEFAULT_TIMEOUT;
        } else if (paramTimeOut < 100) {
            return paramTimeOut * 1000;
        } else {
            return paramTimeOut;
        }
    }

    /**
     * 兼容老的api
     *
     * @param maxRedirections
     * @return
     */
    public RedisClusterBuilder setMaxRedirections(final int maxRedirections) {
        return setMaxAttempts(maxRedirections);
    }

    /**
     * 节点定位重试次数:默认5次
     */
    public RedisClusterBuilder setMaxAttempts(final int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    /**
     * 是否开启统计
     *
     * @param clientStatIsOpen
     * @return
     */
    public RedisClusterBuilder setClientStatIsOpen(boolean clientStatIsOpen) {
        this.clientStatIsOpen = clientStatIsOpen;
        return this;
    }

    /**
     * 是否开启异常统计
     *
     * @param clientStatIsOpen
     * @return
     */
    public RedisClusterBuilder setExcetpionStatIsOpen(boolean clientStatIsOpen) {
        this.clientStatIsOpen = clientStatIsOpen;
        return this;
    }

    /**
     * 是否开启耗时和值大小统计
     *
     * @param costAndValueStatsIsOpen
     * @return
     */
    public RedisClusterBuilder setCostAndValueStatsIsOpen(boolean costAndValueStatsIsOpen) {
        this.costAndValueStatsIsOpen = costAndValueStatsIsOpen;
        return this;
    }
}
