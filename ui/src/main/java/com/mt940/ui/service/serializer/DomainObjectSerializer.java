package com.mt940.ui.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mt940.ui.domain.json.FieldName;
import com.mt940.ui.domain.json.UISerializable;
import com.mt940.ui.service.FormatHelper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;


public class DomainObjectSerializer extends JsonSerializer<UISerializable> {
    @Override
    public void serialize(UISerializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();

        for (Field field : value.getClass().getDeclaredFields()) {
            try {
                FieldName annotation = field.getAnnotation(FieldName.class);
                String fieldName = annotation.value();
                gen.writeStartObject();
                gen.writeStringField("name", fieldName);
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);

                Object fieldValue = getFieldValue(value, field);

                serializers.defaultSerializeField("value", fieldValue, gen);

                boolean editable = false;
                SecurityContext securityContext = SecurityContextHolder.getContext();
                if (securityContext != null) {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication != null) {
                        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                        if (authorities.contains(new SimpleGrantedAuthority("ROLE_UI_ADMIN"))) {
                            editable = annotation.isEditable();
                        }
                    }
                }
                gen.writeBooleanField("editable", editable);
                field.setAccessible(isAccessible);
                gen.writeEndObject();
            } catch (Exception e) {
                //do nothing
            }
        }

        gen.writeEndArray();
    }

    private Object getFieldValue(UISerializable value, Field field) throws IllegalAccessException {
        Class<?> type = field.getType();
        Object fieldValue = field.get(value);
        if (type.isAssignableFrom(LocalDateTime.class)) {
            DateTimeFormat dateTimeFormatAnnotation = field.getAnnotation(DateTimeFormat.class);
            if (dateTimeFormatAnnotation != null ) {
                fieldValue = FormatHelper.format((LocalDateTime) fieldValue, dateTimeFormatAnnotation.pattern());
            }
        }
        return fieldValue;
    }
}
