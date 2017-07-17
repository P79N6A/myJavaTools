package com.commons.javacode;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

/**
*
*
* Gson工具类
* @version   1.0
**/

public class GsonUtil {
	private static Log log = LogFactory.getLog(GsonUtil.class);

	private static Gson gson = new Gson();

	public static <T> T fromGson(String sourceGson,Class<T> targetClass){
		return gson.fromJson(sourceGson, targetClass);
	}

	public static String toGson(Object o){
		return gson.toJson(o);
	}

	public static <T> T[] fromGsonArray(String sourceGson, Class<T[]> targetClass){
		return gson.fromJson(sourceGson, targetClass);
	}

	public static String toGson(Object o, Type targetClass){
		return gson.toJson(o, targetClass);
	}

	public static <T> List<T> fromGsonList(String sourceGson, Type type){
		return gson.fromJson(sourceGson, type);
	}

}
