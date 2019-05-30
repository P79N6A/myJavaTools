/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.commons.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;


public class EmailUtils {
    private static Log log = LogFactory.getLog(EmailUtils.class);

    public static void send(String html, List<String> desList, List<String> ccList, String subject){
        log.info("begin send main subject: " + subject);
        try{
            HtmlEmail email = new HtmlEmail();
            email.setCharset("utf-8");
            email.setHostName("transport.mail.gc.com");
            email.setSmtpPort(25);
            email.setAuthenticator(new org.apache.commons.mail.DefaultAuthenticator("TVT", "123!@#qwe"));
            email.setFrom("TECHPORT@gc.com");
            email.setSubject(subject);

            List<InternetAddress> inter = new ArrayList<InternetAddress>();
            for(String des : desList){
                inter.add(new InternetAddress(des));
            }
            List<InternetAddress> ccAdd = new ArrayList<InternetAddress>();
            for(String des : ccList){
            	ccAdd.add(new InternetAddress(des));
            }
            email.setTo(inter);
            email.setCc(ccAdd);
            email.setHtmlMsg(html);

            String rs = email.send();
            log.info("send mail result=" + rs);
        }catch (Exception e) {
            log.error("send mail error", e);
        }
    }
}

 