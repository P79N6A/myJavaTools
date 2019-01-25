package com.commons.http;

import java.util.ResourceBundle;

public class ConstUtils {
    public static final int HTTP_CONN_TIMEOUT;
    public static final int HTTP_SOCKET_TIMEOUT;
    public static final int MEMCACHED_CONN_TIMEOUT;
    public static final int MEMCACHED_POOL_SIZE;
    public static final String CLIENT_VERSION;
    public static final String DOMAIN_URL;
    public static final String MEMCACHED_URL;
    public static final String REDIS_CLUSTER_URL;
    public static final String REDIS_SENTINEL_URL;
    public static final String REDIS_STANDALONE_URL;
    public static final String CACHECLOUD_REPORT_URL;

    public ConstUtils() {
    }

    static {
        ResourceBundle rb = ResourceBundle.getBundle("cacheCloudClient");
        HTTP_CONN_TIMEOUT = Integer.valueOf(rb.getString("http_conn_timeout"));
        HTTP_SOCKET_TIMEOUT = Integer.valueOf(rb.getString("http_socket_timeout"));
        MEMCACHED_CONN_TIMEOUT = Integer.valueOf(rb.getString("memcached_conn_timeout"));
        MEMCACHED_POOL_SIZE = Integer.valueOf(rb.getString("memcached_pool_size"));
        CLIENT_VERSION = rb.getString("client_version");
        DOMAIN_URL = rb.getString("domain_url");
        MEMCACHED_URL = DOMAIN_URL + rb.getString("memcached_suffix") + CLIENT_VERSION;
        REDIS_CLUSTER_URL = DOMAIN_URL + rb.getString("redis_cluster_suffix") + CLIENT_VERSION;
        REDIS_SENTINEL_URL = DOMAIN_URL + rb.getString("redis_sentinel_suffix") + CLIENT_VERSION;
        REDIS_STANDALONE_URL = DOMAIN_URL + rb.getString("redis_standalone_suffix") + CLIENT_VERSION;
        CACHECLOUD_REPORT_URL = rb.getString("cachecloud_report_url");
    }
}