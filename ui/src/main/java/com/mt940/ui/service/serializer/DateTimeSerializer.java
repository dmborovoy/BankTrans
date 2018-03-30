package com.mt940.ui.service.serializer;


import java.time.ZonedDateTime;

public class DateTimeSerializer extends AbstractSpringSerializer<ZonedDateTime> {

    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }
}
