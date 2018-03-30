package com.mt940.ui.converter;

import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.ui.domain.json.EnumItem;
import org.springframework.core.convert.converter.Converter;

public class MT940TransactionStatusToEnumItemConverter implements Converter<MT940TransactionStatus, EnumItem> {
    @Override
    public EnumItem convert(MT940TransactionStatus src) {
        EnumItem dest = new EnumItem();
        dest.setName(src.name());
        dest.setValue(src.getCode());

        return dest;
    }
}
