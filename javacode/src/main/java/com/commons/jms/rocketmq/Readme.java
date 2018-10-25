package com.commons.jms.rocketmq;

public class Readme {
    /*
    可以看SH公司的mq-client-1.9.8.jar包，是对rocketmq的封装

    使用方法：
            Producer producer = new Producer("xxx-producer", "xxx-topic");
        // 注意，只用启动一次
        producer.start();

        Map<String, Object> message = new HashMap<String, Object>();
        message.put("vid", "123456");
        message.put("aid", "789172");
        //这里message推荐使用map，当然也可以使用json
        Result<SendResult> sendResult = producer.publish(message);
        if(!sendResult.isSuccess){
            //失败消息处理
        }


        Consumer consumer = new Consumer("xxx-consumer", "xxx-topic");
        // 设置消费回调
        consumer.setConsumerCallback(new ConsumerCallback<Map<String, Object>, MessageExt>() {
            public void call(Map<String, Object> t, MessageExt k) {
                try {
                    // 消费逻辑
                } catch (Exception e) {
                    logger.error("consume err, msgid:{}, msg:{}", k.getMsgId(), t, e);
                    // 如果需要重新消费，这里需要把异常抛出，消费失败的消息将发回rocketmq，重试消费
                    throw e;
                }
            }
        });
        // 注意，只用启动一次
        consumer.start();
     */
}
