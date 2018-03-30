package com.mt940.ui.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ProfileEnvironmentContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    Logger l = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        l.info("*************************************************");
        l.info("active environment profiles are [{}]", configurableApplicationContext.getEnvironment().getActiveProfiles());
        l.info("use -Dspring.profiles.active=live or OS environment variable SPRING_PROFILES_ACTIVE to set the live profile");
        l.info("*************************************************");
    }
}
