package com.mt940.ui.service.serializer;


import java.time.LocalTime;


public class LocalTimeSerializer extends AbstractSpringSerializer<LocalTime> {
    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }
}
