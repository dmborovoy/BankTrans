package com.mt940.ui.converter;

import com.mt940.domain.enums.Instance;
import com.mt940.ui.domain.json.EnumItem;
import org.springframework.core.convert.converter.Converter;

public class InstanceToEnumItemConverter implements Converter<Instance, EnumItem> {
    @Override
    public EnumItem convert(Instance src) {
        EnumItem dest = new EnumItem();

        dest.setName(src.name());
        dest.setValue(src.getCode());

        return dest;
    }
}
