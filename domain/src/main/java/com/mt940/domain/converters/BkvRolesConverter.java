package com.mt940.domain.converters;


import com.mt940.domain.enums.BKVRoles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BkvRolesConverter implements AttributeConverter<BKVRoles, Integer> {
    @Override
    public Integer convertToDatabaseColumn(BKVRoles bkvRoles) {
        return bkvRoles.getCode();
    }

    @Override
    public BKVRoles convertToEntityAttribute(Integer integer) {
        return BKVRoles.findByCode(integer);
    }
}
