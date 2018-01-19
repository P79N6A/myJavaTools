/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.commons.javacode;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uhos.blog.util.StringUtil;
import com.uhos.spaces.core.CdnParamInfo;

/**
 * <p>
 * Description:
 * </p>
 */
public class CommonUtils {
    private static final Log log = LogFactory.getLog(CommonUtils.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Base64 base64=new Base64();

    public static String getHostName(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch (Exception e) {
            log.error("getHostName", e);
        }
        return null;
    }

    public static int float2Int(float f){
        return (int)Math.floor(f);
    }

    /**
     * <p>Description:获取ip</p>
     * @param request
     * @return
     * @date 2012-7-17 下午1:45:30
    */
   public static String getIp(HttpServletRequest request) {
       String ip = request.getHeader("X-Forwarded-For");
       String regex="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
       try{
           if(!StringUtil.isEmpty(ip)){
               String[] ips = ip.split(",");
               for(String tIp:ips){
                   tIp =tIp.trim();
                   if(tIp.matches(regex)==true){
                       return tIp;
                   }
               }

           }
       }catch(Exception ex){
           log.error(" getIp error: ip:"+ip, ex);
           ip = StringUtils.defaultIfEmpty(ip, "");
           ip = StringUtils.substringBefore(ip, ",");
       }
       return ip;
   }

   public static String parseDate(Long time){
       if(time == null) return null;
       return sdf.format(new Date(time));
   }

   public static String base64Decode(String str) {
       return new String(base64.decode(str.getBytes()));
   }

   public static String base64Encode(String str) {
       return new String(base64.encode(str.getBytes()));
   }

   public static CdnParamInfo parseCdnParamInfo(String callbackBody){

       return null;
   }

   /**
    * HMAC-SHA1加密方案<br>
    * @param content-待加密内容
    * @param secretKey-密钥
    * @return HMAC_SHA1加密后的字符串
    */
   public static String HMACSHA1(String content, String secretKey) {
       try {
           byte[] secretKeyBytes = secretKey.getBytes();
           SecretKey secretKeyObj = new SecretKeySpec(secretKeyBytes, "HmacSHA1");
           Mac mac = Mac.getInstance("HmacSHA1");
           mac.init(secretKeyObj);
           byte[] text = content.getBytes("UTF-8");
           byte[] encryptContentBytes = mac.doFinal(text);
           //SHA1算法得到的签名长度，都是160位二进制码，换算成十六进制编码字符串表示
           String encryptContent = bytesToHexString(encryptContentBytes);
           return encryptContent;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return content;
   }
   /**
    * 获取字节数组的16进制字符串表示形式<br>
    * 范例：0xff->'ff'
    * @param bytes 字节数组
    * @return string-16进制的字符串表示形式
    */
   private static String bytesToHexString(byte[] bytes) {
       StringBuilder hexString = new StringBuilder("");
       for(byte ib : bytes) {
           char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
           char[] ob = new char[2];
           ob[0] = Digit[(ib >>> 4) & 0X0f];
           ob[1] = Digit[ib & 0X0F];
           hexString.append(ob);
       }
       return hexString.toString();
    }

   public static int getVerType(String version) {
       if("n".equals(version))
           return 2;
       else if("h".equals(version))
           return 1;
       else if("s".equals(version))
           return 21;
       else if("o".equals(version))
           return 31;
       return 2;
   }
   
    public static void main(String[] args) {
        System.out.println(sdf.format(new Date()));
//        float f = -1.38f;
//        System.out.println(CommonUtils.float2Int(f));
//        System.out.println((int)f);
    }

    public static String getBodyString(HttpServletRequest request){
        String jsonInfo = "";
        try {
            InputStream is = request.getInputStream();
            InputStreamReader in = new InputStreamReader(is, "utf-8");
            StringWriter sw = new StringWriter();

            char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                sw.write(buffer, 0, n);
            }

            jsonInfo = sw.toString();
            log.info("jsonInfo:" + jsonInfo);
        } catch (Exception e) {
            log.error("get inputstream", e);
        }
        return jsonInfo;
    }

}

