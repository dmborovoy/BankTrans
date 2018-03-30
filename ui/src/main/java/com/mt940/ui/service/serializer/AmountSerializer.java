package com.mt940.ui.service.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mt940.ui.domain.Amount;

import java.io.IOException;


public class AmountSerializer extends JsonSerializer<Amount> {
    @Override
    public void serialize(Amount value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(String.format("%.2f (%s)", value.getAmount(), value.getCurrency()));
    }

    @Override
    public Class<Amount> handledType() {
        return Amount.class;
    }
}
