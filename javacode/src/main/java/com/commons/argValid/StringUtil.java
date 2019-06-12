package com.commons.argValid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * @author ouzhb
 */
public class StringUtil {

	/**
	 * 判断字符串是否为null、“ ”、“null”
	 * @param obj
	 * @return
	 */
	public static boolean isNull(String obj) {
		if (obj == null){
			return true;
		}else if (obj.toString().trim().equals("")){
			return true;
		}else if(obj.toString().trim().toLowerCase().equals("null")){
			return true;
		}
		
		return false;
	}

	/**
	 * 正则验证是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[+-]?[0-9]+[0-9]*(\\.[0-9]+)?");
		Matcher match = pattern.matcher(str);
		
		return match.matches();
	}
    /** 
     * 将一个长整数转换位字节数组(8个字节)，b[0]存储高位字符，大端 
     *  
     * @param l 
     *            长整数 
     * @return 代表长整数的字节数组 
     */  
    public static byte[] longToBytes(long l) {  
        byte[] b = new byte[8];  
        b[0] = (byte) (l >>> 56);  
        b[1] = (byte) (l >>> 48);  
        b[2] = (byte) (l >>> 40);  
        b[3] = (byte) (l >>> 32);  
        b[4] = (byte) (l >>> 24);  
        b[5] = (byte) (l >>> 16);  
        b[6] = (byte) (l >>> 8);  
        b[7] = (byte) (l);  
        return b;  
    }


	public static List<String> stringToList(String strs){
		String str[] = strs.split(",");
		return Arrays.asList(str);
	}

	public static List<Integer> stringToListInt(String strs){
		String str[] = strs.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for (String s : str){
			list.add(Integer.parseInt(s));
		}
		return list;
	}

	public static String listToString(List<String> list){
		if(list==null){
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean first = true;
		//第一个前面不拼接","
		for(String string :list) {
			if(first) {
				first=false;
			}else{
				result.append(",");
			}
			result.append(string);
		}
		return result.toString();
	}
}
