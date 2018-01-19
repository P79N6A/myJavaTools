/**
 *
 */
package com.commons.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

public class NewHttpClientUtil2 {
    private static Logger log = Logger.getLogger(NewHttpClientUtil2.class);
    private static final String PRIVATE_KEY = "s54!sjfl*3ldf";
    private final CloseableHttpClient httpclient;
    private final RequestConfig defaultRequestConfig;
    private final RequestConfig requestConfig;
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
    private static NewHttpClientUtil2 _instance;

    private NewHttpClientUtil2() {
        poolingHttpClientConnectionManager.setMaxTotal(1500);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(40);
        defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setConnectionRequestTimeout(60 * 1000)
                .build();
        httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
    }

    /**
     * @return
     * @throws Exception
     */
    public static NewHttpClientUtil2 getInstance() {
        if (_instance == null) {
            synchronized (NewHttpClientUtil2.class) {
                if (_instance == null) {
                    _instance = new NewHttpClientUtil2();
                }
            }
        }
        return _instance;
    }

    public String sendRequestAsGet(String url) {
        String rs = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpclient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            log.info("status:" + status + ",url:" + url);

            HttpEntity entity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuilder resBuffer = new StringBuilder();
            String resTemp = "";
            while ((resTemp = br.readLine()) != null) {
                resBuffer.append(resTemp);
            }

            rs = resBuffer.toString();

            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error(url + " sendRequestAsPost:", e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                log.error("error:", e);
            }
        }
        log.info("url:" + url + ",rs:" + rs);
        return rs;
    }

    public String sendRequestAsPost(String url, List<NameValuePair> nvps) {
        String rs = null;
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        InputStream content = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            log.info("status:" + status + ",url:" + url);

            HttpEntity entity = response.getEntity();
            content = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(content, "UTF-8"));
            StringBuilder resBuffer = new StringBuilder();
            String resTemp = "";
            while ((resTemp = br.readLine()) != null) {
                resBuffer.append(resTemp);
            }

            rs = resBuffer.toString();

            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error(url + " sendRequestAsPost:", e);
            httpPost.abort();
            return null;
        } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (Exception e) {

                    }
                }
                if(content != null){
                    try {
                        content.close();
                    } catch (Exception e) {

                    }
                }
                if (httpPost != null) {
                    try {
                        httpPost.releaseConnection();
                    } catch (Exception e) {

                    }
                }

        }
        log.info("url:" + url + ",rs:" + rs);
        return rs;
    }

    /**
     * get添加安全验证
     */
/*    private static String getSecurityUrl() {
        try {
            String ip = TransferHelper.getLocalIP();
            long t = System.currentTimeMillis();
            StringBuffer sBuffer = new StringBuffer();
            String sig = MD5.crypt(ip + t + PRIVATE_KEY);
            sBuffer.append("&sip=").append(ip).append("&t=").append(t).append("&sig=").append(sig);
            return sBuffer.toString();
        } catch (Exception e) {
            log.error("generateKey error", e);
        }
        return null;
    }*/

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<NameValuePair> nvps = null; //TransferHelper.getSecurityKey();
        nvps.add(new BasicNameValuePair("id", "60269829"));
        String rs = null;
        try {
            rs = NewHttpClientUtil2.getInstance().sendRequestAsPost("http://127.0.0.1/upload/getVideoinfo.do",
                    nvps);
            // rs =
            // NewHttpClientUtil2.getInstance().sendRequestAsGet("http://127.0.0.1/upload/getVideoinfo.do");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        JSONObject jsonObject = JSONObject.fromObject(rs);

    }

}
