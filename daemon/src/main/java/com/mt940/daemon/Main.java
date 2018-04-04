package com.mt940.daemon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class Main {
    public static void main(final String[] args) {
        log.info("========================");
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/daemon-context.xml");
        context.registerShutdownHook();
    }
}
