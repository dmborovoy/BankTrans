package com.mt940.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("unchecked")
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
        properties.keySet().stream()
                .map(p -> p + "=" + properties.get(p))
                .sorted()
                .forEach(e -> log.debug("{}", e));
//        if (propertiesLoggingEnabled) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("PROPERTIES:");
//            List tmp = Collections.list(properties.keys());
//            if (sortingEnabled) {
//                Collections.sort(tmp);
//            }
//            Iterator it = tmp.iterator();
//            while (it.hasNext()) {
//                String element = (String) it.next();
//                stringBuilder.append("\r\n");
//                stringBuilder.append(element);
//                stringBuilder.append("=");
//                stringBuilder.append(passwordMaskingEnabled & element.toLowerCase().contains(PASSWORD_KEY) ? MASKED_PASSWORD : properties.get(element));
//            }
//            log.debug("{}", stringBuilder.toString());
//        }
        return properties;
    }
}
