package com.mt940.ui.service.serializer;

import java.time.LocalDate;


public class LocalDateSerializer extends AbstractSpringSerializer<LocalDate> {
    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }
}
