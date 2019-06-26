package com.commons.javacode.token;

import com.commons.javacode.MD5;
import org.springframework.util.DigestUtils;

public class SimpleToken {

    private static String getSecurityUrl() {
        try {
            String ip = EncoderHelper.getLocalIP();
            long t = System.currentTimeMillis();
            StringBuffer sBuffer = new StringBuffer();
            String sig = MD5.crypt(ip + t + PRIVATE_KEY);
            sBuffer.append("?sip=").append(ip).append("&t=").append(t).append("&sig=").append(sig);
            return sBuffer.toString();
        } catch (Exception e) {
            log.error("generateKey error", e);
        }
        return null;
    }

    public static void main(String[] args){
        boolean checkExpire = (System.currentTimeMillis()/1000 + 10*60)- t > 0;
        String PRIVATE_KEY = "s54!sjfl*3ldf";
        String sig2 = DigestUtils.md5Hex(t + PRIVATE_KEY);
        if(!checkExpire ||!sig2.equals(sig)){
            rs.put("status", 0);
            rs.put("msg", "sig err");
            return rs;
        }
    }

}
