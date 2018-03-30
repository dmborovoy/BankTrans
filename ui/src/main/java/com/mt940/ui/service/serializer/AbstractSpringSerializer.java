package com.mt940.ui.service.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import java.io.IOException;


public abstract class AbstractSpringSerializer<T> extends JsonSerializer<T> {
    @Autowired
    ConversionService conversionService;

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(conversionService.convert(value, String.class));
    }

    @Override
    public abstract Class<T> handledType();
}
