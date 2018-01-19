package com.commons.javacode;

import java.net.InetAddress;

/**
 * Key Generator, 产生图片唯一id,作为文件名
 *
 */
public class NameGenerator {
    private static final long IP;
    private static final int SIP;
    static {
        long ipadd;
        byte bip;
        try {
            ipadd = toLong(InetAddress.getLocalHost().getAddress());
            bip = InetAddress.getLocalHost().getAddress()[3];
        } catch (Exception e) {
            ipadd = 0;
            bip = 0;
        }
        IP = ipadd;
        if (bip < 0) {
            SIP = bip + 256;
        } else {
            SIP = bip;
        }
    }

    private static short counter = (short) 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    private NameGenerator() {
    }

    /**
     * 输出字符串
     *
     * @return
     */
    public static String generateName(String passportUID) {
        long keyValue = IP + JVM + getCount() + System.currentTimeMillis();
        if (passportUID != null && !"".equals(passportUID))
            return passportUID + "_" + Long.toHexString(keyValue) + "g" + SIP;
        else
            return Long.toHexString(keyValue) + "g" + SIP;
    }

    private static short getCount() {
        synchronized (NameGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }

    /**
     * @param bytes
     *            byte[]
     * @return int
     */
    private static long toLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = (result << 8) - Byte.MIN_VALUE + bytes[i];
        }
        return result;
    }
}
