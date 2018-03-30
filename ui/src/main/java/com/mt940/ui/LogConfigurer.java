package com.mt940.ui;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.util.Assert;

import java.io.File;

public class LogConfigurer {

    private static final long DELAY = 60000;

    public void start() {
        String home = System.getProperty("nxs.home");
        System.out.println("nxs.home=" + home);
        Assert.notNull(home);

        String log4jConfigFilePath = home + (home.endsWith("/") ? "" : "/") + "bkv-ui.log4j.xml";
        System.out.println("log4j config: " + log4jConfigFilePath);
        if (new File(log4jConfigFilePath).exists()) {
            DOMConfigurator.configureAndWatch(log4jConfigFilePath, DELAY);
        } else {
            System.out.println("Log4j configuration file not found: " + log4jConfigFilePath);
        }
    }

}
