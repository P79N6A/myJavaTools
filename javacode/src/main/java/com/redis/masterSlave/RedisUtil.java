package com.redis.masterSlave;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

public class RedisUtil {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private GenericObjectPoolConfig poolConfig;
    private JedisSentinelPool sentinelPool;
    private JedisPool jedisPool;

    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;
    private String password = "7b50363550974c4a96e8c0421c6d9b4a";
    private static String REDISAPI = "https://panther.sohurdc.com/api/redis/release?uid=20282";


    public void init() {
        poolConfig = new JedisPoolConfig();
//      poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        poolConfig.setMaxWaitMillis(1000L);
//        poolConfig.setJmxNamePrefix("jedis-sentinel-pool");
//        poolConfig.setJmxEnabled(true);

        build();
    }

    public JedisPool build() {
        if (jedisPool == null) {
            RedisConfig redisConfig = getRedisConfig();
            String host = "mb.y.redis.sohucs.com";
            int port = 22911;
            if(redisConfig == null){
                host = redisConfig.getHost();
                port = redisConfig.getPort();
            }

            jedisPool = new JedisPool(poolConfig, host, port,
                    soTimeout, password);
        }
        return jedisPool;
    }

    private RedisConfig getRedisConfig(){
        try {
            String rs = HttpClientManager.getInstance().sendRequestAsGet(REDISAPI);
            log.info("redis config," + rs);
            JSONArray jobjs = new JSONArray(rs);
            for(int i = 0; i < jobjs.length(); i++){
                JSONObject o = jobjs.getJSONObject(i);
                int master = o.getInt("master");
                if(master == 1){
                    String ip = o.getString("ip");
                    int port = o.getInt("port");
                    return new RedisConfig(ip, port);
                }
            }
        } catch (Throwable e) {
            log.error("get redis config", e);
        }
        return null;
    }

    public static void main(String[] args){
        String rs = HttpClientManager.getInstance().sendRequestAsGet(REDISAPI);
        System.out.println(rs);

        RedisUtil ru = new RedisUtil();
        Jedis jedis = ru.getJedisPool().getResource();
        jedis.close();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }


    public class RedisConfig{
        private String host;
        private int port;

        public RedisConfig(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
