package com.mt940.daemon.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/daemon-context.xml")
public class CommonConfig {
}
