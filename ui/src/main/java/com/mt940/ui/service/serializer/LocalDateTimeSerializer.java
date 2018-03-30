package com.mt940.ui.service.serializer;

import java.time.LocalDateTime;


public class LocalDateTimeSerializer extends AbstractSpringSerializer<LocalDateTime> {
    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }
}
