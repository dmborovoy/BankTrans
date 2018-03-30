package com.mt940.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String HORIZONTAL_LINE = "\n=========================================================";

    private Main() {
    }

    public static void main(final String... args) {
        LOGGER.info(HORIZONTAL_LINE);
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/daemon-context.xml");
        context.registerShutdownHook();
    }
}
