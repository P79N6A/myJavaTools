/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package spring.hibernate5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.uhos.ccm.heart.utils.CommandCustomer;

/**
 * <p>
 * Description:
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年3月31日
 */
@SpringBootApplication
@ImportResource(locations={"classpath:applicationContext.xml"})
public class CommandMainServer {
    public static final Log log = LogFactory.getLog(CommandMainServer.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CommandMainServer.class);
        ConfigurableApplicationContext context = builder.bannerMode(Banner.Mode.OFF).run(args);

        //ctx.start();


        try {
            CommandCustomer commandCustomer = context.getBean(CommandCustomer.class);
            commandCustomer.createConsumer();
            //new CommandCustomer().createConsumer();
            log.info("start success......");
            Thread.sleep(Long.MAX_VALUE);
        } catch (Throwable e) {
            log.error("", e);
        }

    }

}

