package com.commons.jms.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.sohu.index.tv.mq.common.ConsumerCallback;
import com.sohu.index.tv.mq.common.Result;
import com.sohu.spaces.cloud.SVCDispatcher;
import com.sohu.spaces.util.CommonUtils;
import com.sohu.tv.mq.rocketmq.Consumer;
import com.sohu.tv.mq.rocketmq.Producer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Component
public class AppPushlishJms {
    private Logger log = LoggerFactory.getLogger(AppPushlishJms.class);

    @PostConstruct
    public void start() {
        try{
            boolean testEnv = CommonUtils.isTestEnv();
            log.info("AppPushlishJms testEnv:" + testEnv);
            Consumer consumer = null;
            if(testEnv) {
                consumer = new Consumer("test-consumer", "test-topic");
            }
            log.info("testEnv:" + testEnv);

            consumer.setConsumerCallback(new ConsumerCallback<Map<String, Object>, MessageExt>() {
                public void call(Map<String, Object> t, MessageExt k) throws Exception {
                    Long vid  = null;
                    try {
                        log.info("receive callback..." + t);
//                        String s = (String) t;
//                        JSONObject json = JSONObject.parseObject(s);
                        vid = (Long)t.get("vid");
                        Integer publishStatus = (Integer)t.get("publishStatus");
                        int wait;
                        if(publishStatus == 1) {
                            wait = 0;
                        }else{
                            wait = -1;
                        }
                        SVCDispatcher.enWaitRequest(vid, wait);
                    } catch (Exception e) {
                        log.error("", e);
                        // 如果需要重新消费，这里需要把异常抛出，消费失败的消息将发回rocketmq，重试消费
                        throw e;
                    }
                    log.info("AppPushlishJms process end,vid:" + vid);
                }
            });
            consumer.start();
            log.info("AppPushlishJms started");
        }catch (Throwable t){
            log.error("", t);
        }
    }

    public static void main(String[] args){
        Producer producer = new Producer("topic-producer",
                "test-topic");
        producer.start();

        Map<String, Object> message = new HashMap<String, Object>();
        message.put("vid", 150005318L);
        message.put("publishStatus", 1);
        Result<SendResult> sendResult = producer.publish(message);
        if(!sendResult.isSuccess){
            System.out.println("fail");
        }
        System.out.println(sendResult);
    }

}
