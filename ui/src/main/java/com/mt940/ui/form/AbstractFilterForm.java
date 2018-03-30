package com.mt940.ui.form;

import java.lang.reflect.Field;


abstract public class AbstractFilterForm {

    public boolean isFilterEmpty() {
        boolean isEmpty = true;
        for (Field field : getClass().getDeclaredFields()) {
            boolean isAccessible = field.isAccessible();
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj != null) {
                    if (obj instanceof String) {
                        String val = (String) field.get(this);
                        if (!val.isEmpty()) {
                            isEmpty = false;
                            break;
                        }
                    } else if (field.getType().isArray()) {
                        Object[] arr = (Object[]) field.get(this);
                        if (arr.length > 0) {
                            isEmpty = false;
                            break;
                        }
                    } else {
                        isEmpty = false;
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                // something goes wrong
            } finally {
                field.setAccessible(isAccessible);
            }
        }

        return isEmpty;
    }
}
