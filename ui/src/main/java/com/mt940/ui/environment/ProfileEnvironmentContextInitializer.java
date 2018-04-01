package com.mt940.ui.environment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class ProfileEnvironmentContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        log.info("*************************************************");
        log.info("active environment profiles are [{}]", configurableApplicationContext.getEnvironment().getActiveProfiles());
        log.info("use -Dspring.profiles.active=live or OS environment variable SPRING_PROFILES_ACTIVE to set the live profile");
        log.info("*************************************************");
    }
}
