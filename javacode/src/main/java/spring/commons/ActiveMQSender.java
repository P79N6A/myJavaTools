package com.uhos.spaces.util;



import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;


public class ActiveMQSender {
    
    private static Log log = LogFactory.getLog(ActiveMQSender.class); 

    private final JmsTemplate jmsTemplate;
    private final Destination topicDestination;

    public ActiveMQSender(final JmsTemplate jmsTemplate, final Destination destination) {
        this.jmsTemplate = jmsTemplate;
        this.topicDestination = destination;
    }

    public void send(final String text) {
        long startTime = System.currentTimeMillis();
        try {
            jmsTemplate.setDefaultDestination(topicDestination);
            jmsTemplate.convertAndSend(text);
            log.info("activemq send: " + text);
        } catch (Exception e) {
            log.info("activemq send: " + text + "error",e);
        }
        long endTime = System.currentTimeMillis();
        log.info("activemq send: " + text + "cost time: " + (endTime-startTime));
    }
    
   
    
}