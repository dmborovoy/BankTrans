package com.mt940.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
public class MT940PropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private final static String PASSWORD_KEY = "pass";
    private final static String MASKED_PASSWORD = "********";
    private boolean propertiesLoggingEnabled = true;
    private boolean sortingEnabled = true;
    private boolean passwordMaskingEnabled = true;

    @Override
    protected Properties mergeProperties() throws IOException {
        final Properties properties = super.mergeProperties();
        String out = properties.keySet().stream()
                .map(p -> p + "=" + properties.get(p))
                .sorted()
                .collect(Collectors.joining("\r\n"));
        log.debug("\r\n{}", out);
        return properties;
    }
}
