/**
 *
 */
package com.commons.http;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * <p>
 * Description: add fail-over and retry feature for http.
 * </p>
 */
public class NewHttpClientUtil2Proxy {
    private static Logger logger = Logger.getLogger(NewHttpClientUtil2Proxy.class);
    private int retryTimes = 3;
    private String domain1;
    private String domain2;
    private final NewHttpClientUtil2 newHttpClientUtil2 = NewHttpClientUtil2.getInstance();
    private CheckType checkType = CheckType.BYJSON;
    private long sleepTime = 100;

    public enum CheckType {
        DEFAULT, BYJSON, BYSTATUS, BYDATA
    }

    public NewHttpClientUtil2Proxy() {

    }

    public NewHttpClientUtil2Proxy(CheckType checkType) {
        if(checkType != null){
            this.checkType = checkType;
        }
    }

    public NewHttpClientUtil2Proxy(CheckType checkType, String domain1, String domain2) {
        if(checkType != null){
            this.checkType = checkType;
        }
        this.domain1 = domain1;
        this.domain2 = domain2;
    }

    private boolean isFailOver() {
        if(StringUtils.isNotBlank(domain1) && StringUtils.isNotBlank(domain2)) {
            return true;
        }
        return false;
    }

    public String sendRequestAsGet(String url) {
        String result = null;
        int i = 0;
        while(i++ < retryTimes){
            result = sendRequestAsGetRetry(url, i);

            if(StringUtils.isNotBlank(result)){
                return result;
            }

            if(isFailOver()) {
                url = url.replace(domain1, domain2);
                result = sendRequestAsGetRetry(url, i);

                if(StringUtils.isNotBlank(result)){
                    return result;
                }
            }

            sleep();
        }

        return result;
    }

    private void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    public String sendRequestAsPost(String url, List<NameValuePair> nvps) {
        String result = null;
        int i = 0;
        while(i++ < retryTimes){
            result = sendRequestAsPostRetry(url, nvps, i);

            if(StringUtils.isNotBlank(result)){
                return result;
            }

            if(isFailOver()) {
                url = url.replace(domain1, domain2);
                result = sendRequestAsPostRetry(url, nvps, i);

                if(StringUtils.isNotBlank(result)){
                    return result;
                }
            }

            sleep();
        }

        return result;
    }

    private String sendRequestAsGetRetry(String url, int time) {
        boolean resultError = true;

        String result = newHttpClientUtil2.sendRequestAsGet(url);
        logger.info("time:" + time + ",url:" + url + ",rs:" + result);

        try {
            if (result != null) {
                if(checkResult(result)){
                    resultError = false;
                }
            }
        } catch (Exception e) {
            logger.error(result, e);
        }

        if(StringUtils.isNotBlank(result) && !resultError){
            return result;
        } else {
            return null;
        }
    }

    private String sendRequestAsPostRetry(String url, List<NameValuePair> params, int time) {
        boolean resultError = true;

        String result = newHttpClientUtil2.sendRequestAsPost(url, params);
        logger.info("time:" + time + ",url:" + url + ",param:" + params.toString() + ",rs:" + result);

        try {
            if (result != null) {
                if(checkResult(result)){
                    resultError = false;
                }
            }
        } catch (Exception e) {
            logger.error(result, e);
        }

        if(StringUtils.isNotBlank(result) && !resultError){
            return result;
        } else {
            return null;
        }
    }

    protected boolean checkResult(String result) {
        if(checkType == CheckType.BYJSON) {
            try {
                new JSONObject(result);
                return true;
            } catch (Exception e) {
                logger.error("json error:" + result, e);
            }
            return false;
        } else if(checkType == CheckType.BYSTATUS){
            try {
                JSONObject jsonRs = new JSONObject(result);
                int v = jsonRs.getInt("status");
                if(v != 1){
                    return false;
                }
            } catch (Exception e) {
                logger.error("json error:" + result, e);
            }
        } else if(checkType == CheckType.BYDATA) {
            try {
                JSONObject jsonRs = new JSONObject(result);
                String v = jsonRs.getString("data");
                if(StringUtils.isBlank(v)){
                    return false;
                }
            } catch (Exception e) {
                logger.error("json error:" + result, e);
            }
        }
        return true;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public void setFailOver(String domain1, String domain2) {
        this.domain1 = domain1;
        this.domain2 = domain2;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
