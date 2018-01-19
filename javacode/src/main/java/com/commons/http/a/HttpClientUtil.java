/**
 * 
 */
package com.commons.http.a;





import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uhos.blog.util.EscapeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;

public class HttpClientUtil {
    private static Log log = LogFactory.getLog(HttpClientUtil.class);
    private  HttpClient httpclient;  
    
    public void setHttpclient(HttpClient httpclient) {  
        this.httpclient = httpclient;  
    }  
  
    public HttpClientUtil() {  
  
    } 
    /** 
     * 以get方式发送http请求 
     *  
     * @param url 
     * @return 
     */  
    public String sendRequestAsGet(String url) {  
        BufferedReader br = null;
        GetMethod getMethod = new GetMethod(url);  
        try {  
            log.info("sendRequestAsGet url:"+url);
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());   
            httpclient.executeMethod(getMethod); 
            InputStream resStream = getMethod.getResponseBodyAsStream();  
            br = new BufferedReader(new InputStreamReader(resStream,"utf-8"));  
            StringBuffer resBuffer = new StringBuffer();  
            String resTemp = "";  
            while((resTemp = br.readLine()) != null){  
                resBuffer.append(resTemp);  
            }  
            String response = resBuffer.toString();  
            log.info("sendRequestAsGet url:" + url + ",rs:" + response);
            return response;
        } catch (Exception e) {  
            log.error(url+" sendRequestAsGet:",e); 
            return null;  
        }  
        finally{  
            getMethod.releaseConnection();
            try {
                if(br != null){
                    br.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }  
    }   
    
    /** 
     * 以post方式发送http请求 
     *  
     * @param url 
     * @return 
     */  
    public int sendRequestAsPost(String url) {  
        PostMethod postMethod = new PostMethod(url);  
        try {  
            postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());  
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(1000);  
            postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,1000);  
  
            int statusCode = httpclient.executeMethod(postMethod);  
            return statusCode;  
        } catch (Exception e) {  
            log.error(url+" sendRequestAsPost:",e); 
            return 500;  
        }  
        finally{  
            postMethod.releaseConnection();  
        }  
    }      

    
    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClient httpclient = new HttpClient();  
        String keyword=EscapeUtil.escape("美女");                
        System.out.println("keyword:"+keyword);
        String url = "http://so.tv.uhos.com/js?chl=&m=1&s=100&p=4&box=1&s=100&p=1&ugc=3&wd="+keyword;  
        System.out.println("url:"+url);
        GetMethod method = new GetMethod(url);  
        try {  
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());  
            int statusCode = httpclient.executeMethod(method);  
            System.out.println(statusCode);  
            byte[] responseBody = method.getResponseBody();  
            
            InputStream resStream = method.getResponseBodyAsStream();  
            BufferedReader br = new BufferedReader(new InputStreamReader(resStream));  
            StringBuffer resBuffer = new StringBuffer();  
            String resTemp = "";  
            while((resTemp = br.readLine()) != null){  
                resBuffer.append(resTemp);  
            }  
            String response = resBuffer.toString();  
            
            String rs=new String(response);
            System.out.println("rs:"+rs);
            JSONObject jsonObject=new JSONObject(rs);
            JSONArray jsonArray=(JSONArray)jsonObject.get("r");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObject=jsonArray.getJSONObject(i);
                System.out.println(jObject.get("id")+",cid:"+jObject.get("cid"));
            }
            System.out.println("length:"+jsonArray.length());
            System.out.println(new String(responseBody));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            method.releaseConnection();  
        } 

    }

}
