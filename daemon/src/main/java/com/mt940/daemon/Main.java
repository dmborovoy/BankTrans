package com.mt940.daemon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class Main {


    private static final String HORIZONTAL_LINE = "\n=========================================================";

    private Main() {
    }

    public static void main(final String... args) {
        log.info(HORIZONTAL_LINE);
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/daemon-context.xml");
        context.registerShutdownHook();
    }
}
