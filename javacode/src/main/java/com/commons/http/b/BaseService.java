/**
 *
 */
package com.uhos.spaces.videos.encoder.base.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uhos.spaces.videos.encoder.base.util.GsonUtil;
import com.uhos.spaces.videos.encoder.base.util.NewHttpClientManager;

/**
 * @author guangchaowu
 */
public class BaseService {
    private static Logger logger = Logger.getLogger(BaseService.class);

    private static final int RETRY_TIMES = 3;
    protected static final String SERVICE_HOST_PRE = "http://inner.my.tv.uhos.com";
    private static final String PRIVATE_KEY = "s54!sjfl*3ldf";

    protected static boolean requestService(String url, Map para) {
        boolean b = false;
        String result = null;
        try {
            result = requestHttp(url, para);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                b = true;
            }
        } catch (Exception e) {
            logger.error("call service error, result" + result, e);
        }

        return b;
    }

    protected static <T> T getObject(Long id, Class<T> targetClass, String url, String sth) {
        T obj = null;
        String result = null;
        try {
            Map map = new HashMap();
            map.put(sth, id);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                JSONObject data = (JSONObject) resultObj.get("data");
                obj = GsonUtil.fromGson(data.toString(), targetClass);
            }
        } catch (Exception e) {
            logger.error("call service error, id:" + id + ", " + result, e);
        }

        return obj;
    }

    protected static <T> boolean updateObject(T obj, Long id, String url, String sth) {
        boolean success = false;
        String result = null;
        try {
            String gson = GsonUtil.toGson(obj);
            Map map = new HashMap();
            map.put(sth, gson);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                success = true;
            }
        } catch (Exception e) {
            logger.error("call service error, id:" + id + ", " + result, e);
        }

        return success;
    }

    protected static <T> T updateObjectAndReturn(T obj, Long id, String url, Class<T> targetClass, String sth) {
        T returnObj = null;
        String result = null;
        try {
            String gson = GsonUtil.toGson(obj);
            Map map = new HashMap();
            map.put(sth, gson);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                JSONObject data = (JSONObject) resultObj.get("data");
                returnObj = GsonUtil.fromGson(data.toString(), targetClass);
            }
        } catch (Exception e) {
            logger.error("call service error, id:" + id + ", " + result, e);
        }

        return returnObj;
    }

    protected static <T> T saveObjectAndReturn(T obj, String url, Class<T> targetClass, String sth) {
        T returnObj = null;
        String result = null;
        String gson = null;
        try {
            gson = GsonUtil.toGson(obj);
            Map map = new HashMap();
            map.put(sth, gson);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                JSONObject data = (JSONObject) resultObj.get("data");
                returnObj = GsonUtil.fromGson(data.toString(), targetClass);
            }
        } catch (Exception e) {
            logger.error("call service error, gson:" + gson + ", " + result, e);
        }

        return returnObj;
    }

    protected static <T> Long saveObject(T obj, String url, String sth) {
        Long id = null;
        String result = null;
        try {
            String gson = GsonUtil.toGson(obj);
            Map map = new HashMap();
            map.put(sth, gson);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                id = resultObj.getLong("data");
            }
        } catch (Exception e) {
            logger.error("call service error, id:" + id + ", " + result, e);
        }

        return id;
    }

    protected static <T> boolean removeObject(Long id, String url, String sth) {
        boolean success = false;
        String result = null;
        try {
            Map map = new HashMap();
            map.put(sth, id);
            result = requestHttp(url, map);
            JSONObject resultObj = new JSONObject(result);
            Integer status = (Integer) resultObj.get("status");
            if (status == 1) {
                success = true;
            }
        } catch (Exception e) {
            logger.error("call service error, id:" + id + ", " + result, e);
        }

        return success;
    }

    protected static <T> List<T> getListObject(String url, Map urlPara, Type type) {
        List<T> list = null;
        String result = null;
        try {
            result = requestHttp(url, urlPara);
            JSONObject resultObj = new JSONObject(result);
            Integer stat = (Integer) resultObj.get("status");
            if (stat == 1) {
                JSONArray data = resultObj.getJSONArray("data");
                list = GsonUtil.fromGsonList(data.toString(), type);
            } else {
                // logger.error("status:" + stat + ", statusText:" +
                // resultObj.get("statusText"));
            }
        } catch (Exception e) {
            logger.error("call service error, parameter:" + urlPara + ", " + result, e);
        }

        return list;
    }

    @SuppressWarnings("rawtypes")
    protected static Serializable getBasicObjectAsPost(String url, Map urlPara, Class targetClass) {
        Serializable obj = null;
        String result = null;
        try {
            result = requestHttp(url, urlPara);
            obj = getBasicObjectCommand(url, targetClass, result);
        } catch (Exception e) {
            logger.error("call service error, parameter:" + urlPara + ", " + result, e);
        }
        return obj;
    }

    @SuppressWarnings("rawtypes")
    protected static Serializable getBasicObject(String url, Map urlPara, Class targetClass) {
        Serializable obj = null;
        String result = null;
        try {
            result = requestHttp(url, urlPara);
            obj = getBasicObjectCommand(url, targetClass, result);
        } catch (Exception e) {
            logger.error("call service error, parameter:" + urlPara + ", " + result, e);
        }

        return obj;
    }

    @SuppressWarnings("rawtypes")
    private static Serializable getBasicObjectCommand(String url, Class targetClass, String result) {
        Serializable obj = null;
        try {
            JSONObject resultObj = new JSONObject(result);
            Integer stat = (Integer) resultObj.get("status");
            if (stat == 1) {
                if (targetClass.getSimpleName().equals(Integer.class.getSimpleName())) {
                    obj = resultObj.getInt("data");
                } else if (targetClass.getSimpleName().equals(Long.class.getSimpleName())) {
                    obj = resultObj.getLong("data");
                } else if (targetClass.getSimpleName().equals(String.class.getSimpleName())) {
                    obj = resultObj.getString("data");
                }
            } else {
                // logger.error("status:" + stat + ", statusText:" +
                // resultObj.get("statusText"));
            }
        } catch (Exception e) {
            logger.error("call service error, " + result, e);
        }

        return obj;
    }

    protected static <T> T getCommonObject(String url, Map urlPara, Class<T> targetClass) {
        T obj = null;
        String result = null;
        try {
            result = requestHttp(url, urlPara);
            JSONObject resultObj = new JSONObject(result);
            Integer stat = (Integer) resultObj.get("status");
            if (stat == 1) {
                JSONObject data = (JSONObject) resultObj.get("data");
                obj = GsonUtil.fromGson(data.toString(), targetClass);
            } else {
                // logger.error("status:" + stat + ", statusText:" +
                // resultObj.get("statusText"));
            }
        } catch (Exception e) {
            logger.error("call service error, parameter:" + urlPara + ", " + result, e);
        }

        return obj;
    }

    protected static String requestHttp(String url) {
        return requestCommand(url, null);
    }

    protected static String requestHttp(String url, Map params) {
        return requestCommand(url, params);
    }

    private static String requestCommand(String url, Map params) {
        String result = null;
        if (params == null) {
            result = NewHttpClientManager.getInstance().sendRequestAsGet(url);
        } else {
            result = NewHttpClientManager.getInstance().sendRequestAsPost(url, params);
        }
        logger.info("url:" + url + ",params:" + params + ",result:" + result);
        boolean resultError = true;
        JSONObject resultObj = null;
        try {
            if (result != null) {
                resultObj = new JSONObject(result);
                resultError = false;
            }
        } catch (JSONException e) {
            logger.error("", e);
        }
        int i = 0;
        while ((result == null || resultError) && i++ < RETRY_TIMES) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e1) {
                logger.error("", e1);
            }
            if (params == null) {
                result = NewHttpClientManager.getInstance().sendRequestAsGet(url);
            } else {
                result = NewHttpClientManager.getInstance().sendRequestAsPost(url, params);
            }
            try {
                if (result != null) {
                    resultObj = new JSONObject(result);
                    resultError = false;
                }
            } catch (JSONException e) {
                logger.error("", e);
            }
        }
        //转码双活
        url = url.replaceAll("inner.my.tv.uhos.com", "inner2.my.tv.uhos.com");
        i = 0;
        while ((result == null || resultError) && i++ < RETRY_TIMES) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e1) {
                logger.error("", e1);
            }
            if (params == null) {
                result = NewHttpClientManager.getInstance().sendRequestAsGet(url);
            } else {
                result = NewHttpClientManager.getInstance().sendRequestAsPost(url, params);
            }
            try {
                if (result != null) {
                	logger.info("inner2 result: " + result);
                    resultObj = new JSONObject(result);
                    resultError = false;
                }
            } catch (JSONException e) {
                logger.error("", e);
            }
        }
        return result;
    }

}
