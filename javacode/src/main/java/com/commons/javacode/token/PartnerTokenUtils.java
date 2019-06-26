package com.commons.javacode.token;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class PartnerTokenUtils {
    private static Logger logger = Logger.getLogger(PartnerTokenUtils.class);

	private static Map<String, String> partnerKeyMap = new HashMap<String, String>();

	static{
		//xiaoyi key1
		partnerKeyMap.put("1001", "w2qr~7A=eR^oP]o?");
		//ugc upload key2
		partnerKeyMap.put("1002", "Ed#~A9=lN[^Kv]qu");
		//
		partnerKeyMap.put("1003", "#Tv_#~42=qWN)&%Mz[p");
	}

	public static boolean checkPartner(String partner){
		return partnerKeyMap.containsKey(partner);
	}


	public static boolean checkExpire(long expire){
		return System.currentTimeMillis() /1000 > expire ? false : true;
	}

	public static boolean checkToken(String partner, long expire, String params, String sign){
		return sign.equals(getToken(partner, expire, params));
	}

	public static String getToken(String partner, long expire, String params){
		return partner + "_" + expire + "_" + DigestUtils.md5Hex(params + partnerKeyMap.get(partner) + expire);
	}

	public static String getSortedParams(TreeMap<String, Object> params){
		// 对参数进行升序排列
        Iterator<String> it = params.keySet().iterator();
        StringBuilder buffer = new StringBuilder(256);
        while (it.hasNext()) {
            String key = it.next();
            if (params.get(key) == null) {
                continue;
            }
            String value = params.get(key).toString().trim();
            // 过滤掉值为空的参数
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            buffer.append(key).append("=").append(value);
            if(it.hasNext()){
            	buffer.append("&");
            }
        }
        if(buffer.length() > 0){
        	return buffer.toString();
        }
        return "";

	}

    public static boolean checkToken(String videoid, String token) {
        try{
            logger.info("check token,id:" + videoid + ",token:" + token);
            TreeMap<String, Object> map = new TreeMap<String, Object>();
            map.put("id", videoid);

            String params = PartnerTokenUtils.getSortedParams(map);

            String[] temps = token.split("_");
            String partner = temps[0];
            long expire = Long.parseLong(temps[1]);
            if(checkPartner(partner) && checkExpire(expire) && checkToken(partner, expire, params, token)){
                return true;
            }
        }catch (Exception e) {
            logger.error("checkToken,vid:" + videoid + ",token:" + token, e);
        }
        return false;
    }

    public static String genToken(String partner, long id) {
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        map.put("id", id);
        String params = getSortedParams(map);

        //过期时间单位秒
        long expire = System.currentTimeMillis()/1000 + 60*60*24*6;
        return getToken(partner, expire, params);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//合作方id，每个合作方会分配一个
		String partner = "1001";

		//对必要参数做排序操作，目前只包括uid、vid
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("id", 79240414);
		String params = getSortedParams(map);
		System.out.println(params);
		//过期时间单位秒
		long expire = System.currentTimeMillis()/1000 + 60*60*24*6;
		String token = getToken(partner, expire, params);
		System.out.println(token);

		/*
		//合作方id，每个合作方会分配一个
        String partner = "1001";

        TreeMap<String, Object> map = new TreeMap<String, Object>();
        map.put("vid", 79240414);
        map.put("uid", 49599546);

        //对必要参数做排序操作，目前只包括uid、vid
        String params = getSortedParams(map);
        //过期时间单位秒
        long expire = System.currentTimeMillis()/1000 - 60*60*24*5;
        String token = getToken(partner, expire, params);
		 */

/*
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(token);

//		token = "1001_1437443661_99daf2c6acbf21717203c87f0b506cc6";

		String[] temps = token.split("_");
		if(checkPartner(temps[0]) && checkExpire(expire) && checkToken(temps[0], Long.parseLong(temps[1]), params, token)){
			System.out.println("true");
		}else{
			System.out.println("false");
		}*/

	    String aa = "1001_86400_907b1eea3f27b3cc00eebb700b09ee73";
	    boolean checkToken = PartnerTokenUtils.checkToken(82784302+"", aa);
	    System.out.println(checkToken);
	    System.out.println(System.currentTimeMillis()/1000 + 60*60*24*6);
	}

}
