package com.redis.cluster;

import com.commons.javacode.CommonUtils;
import com.redis.ClientBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.PipelineCluster;

import javax.annotation.PostConstruct;

/**
 */
@Service
public class RedisClusterDemo {
    private static Logger log = LoggerFactory.getLogger(RedisService.class);

    private static PipelineCluster jedisPool;

    @PostConstruct
    private void initRedis(){
        //根据自己需要设置poolConfig
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        poolConfig.setMaxWaitMillis(1000L);
        poolConfig.setJmxEnabled(true);
        try {
            long appId = 0;
            boolean testEnv = CommonUtils.isTestEnv();
            if(testEnv) {
                appId = 10730;
            } else {
                appId = 10731;
            }
            log.info("appId:" + appId + ",testEnv:" + testEnv);
            //根据自己需要设置超时时间
            jedisPool = ClientBuilder.redisCluster(appId)
                    .setTimeout(2000)
                    .setJedisPoolConfig(poolConfig)
                    .setMaxRedirections(3)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static PipelineCluster getRedis(){
        return jedisPool;
    }

    public static String getString(String key){
        Jedis resource = null;
        try {
            String v = jedisPool.get(key);
            log.info("redis get key:" + key + ",value:" + v);
            return v;
        }catch (Throwable t){
            log.error("redis get key:" + key, t);
        }finally {
            if(resource != null){
                resource.close();
            }
        }
        return "ERROR";
    }

    public static void del(String key){
        try {
            jedisPool.del(key);
            log.info("redis del key:" + key);
        }catch (Throwable t){
            log.error("redis del key:" + key, t);
        }finally {
//            if(resource != null){
//                resource.close();
//            }
        }
    }

    public static void main(String[] args){
        RedisService rs = new RedisService();
        rs.initRedis();
        String v = rs.getString("pb_150005303");
        System.out.println(v);
    }

}
