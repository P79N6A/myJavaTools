package com.commons.http;

import com.sohu.tv.cachecloud.client.basic.exception.CacheCloudClientHttpUtilsException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


/*
 * cc client jar中的类，用URLEncoder实现的
 */
public final class HttpUtils {
    public HttpUtils() {
    }

    public static String doPost(String reqUrl, Map<String, String> parameters) {
        String encoding = "UTF-8";
        return doPost(reqUrl, parameters, encoding, ConstUtils.HTTP_CONN_TIMEOUT, ConstUtils.HTTP_SOCKET_TIMEOUT);
    }

    public static String doPost(String reqUrl, Map<String, String> parameters, String encoding) {
        return doPost(reqUrl, parameters, encoding, ConstUtils.HTTP_CONN_TIMEOUT, ConstUtils.HTTP_SOCKET_TIMEOUT);
    }

    public static String doPost(String reqUrl, Map<String, String> parameters, String encoding, int connectTimeout, int readTimeout) {
        HttpURLConnection urlConn = null;

        String var7;
        try {
            urlConn = sendPost(reqUrl, parameters, encoding, connectTimeout, readTimeout);
            String responseContent = getContent(urlConn, encoding);
            var7 = responseContent.trim();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }

        }

        return var7;
    }

    private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters, String encoding, int connectTimeout, int readTimeout) {
        HttpURLConnection urlConn = null;

        try {
            String params = generatorParamString(parameters, encoding);
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setDoOutput(true);
            byte[] b = params.getBytes(encoding);
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();
            return urlConn;
        } catch (Exception var9) {
            throw new CacheCloudClientHttpUtilsException(var9.getMessage(), var9);
        }
    }

    private static String getContent(HttpURLConnection urlConn, String encoding) {
        try {
            String responseContent = null;
            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();

            for(String crlf = System.getProperty("line.separator"); tempLine != null; tempLine = rd.readLine()) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
            }

            responseContent = tempStr.toString();
            rd.close();
            in.close();
            return responseContent;
        } catch (Exception var8) {
            throw new CacheCloudClientHttpUtilsException(var8.getMessage(), var8);
        }
    }

    public static String doGet(String link, String encoding, int connectTimeout, int readTimeout) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(link);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            boolean var9 = false;

            int i;
            while((i = in.read(buf)) > 0) {
                out.write(buf, 0, i);
            }

            out.flush();
            String s = new String(out.toByteArray(), encoding);
            String var10 = s;
            return var10;
        } catch (Exception var14) {
            throw new CacheCloudClientHttpUtilsException(var14.getMessage(), var14);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }

        }
    }

    public static String doGet(String link) {
        String encoding = "UTF-8";
        return doGet(link, encoding, ConstUtils.HTTP_CONN_TIMEOUT, ConstUtils.HTTP_SOCKET_TIMEOUT);
    }

    private static String generatorParamString(Map<String, String> parameters, String encoding) {
        StringBuffer params = new StringBuffer();
        if (parameters != null) {
            Iterator iter = parameters.keySet().iterator();

            while(iter.hasNext()) {
                String name = (String)iter.next();
                String value = (String)parameters.get(name);
                params.append(name + "=");

                try {
                    params.append(URLEncoder.encode(value, encoding));
                } catch (UnsupportedEncodingException var8) {
                    throw new RuntimeException(var8.getMessage(), var8);
                } catch (Exception var9) {
                    String message = String.format("'%s'='%s'", name, value);
                    throw new RuntimeException(message, var9);
                }

                if (iter.hasNext()) {
                    params.append("&");
                }
            }
        }

        return params.toString();
    }
}
