package com.mt940.domain.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum BKVRoles {
    SUPER_USER(0, true),
    UI_ADMIN(1, true),
    UI_USER(2, false),
    UI_GUEST(3, false),
    API_ADMIN(100, true),
    API_PING(101, false),
    API_GET_TRANSACTION(102, false),
    API_CONFIRM_TRANSACTION(103, false);

    final int code;
    final boolean macro;

    BKVRoles(int code, boolean macro) {
        this.code = code;
        this.macro = macro;
    }

    public static BKVRoles findByCode(int code) {
        for (BKVRoles value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    //    TODO DB: a little bit ugly. Make it more elegant
    public static Set<BKVRoles> expandRole(BKVRoles role) {
        Set<BKVRoles> result = new HashSet<BKVRoles>();
        if (role == SUPER_USER) {
            result.addAll(Arrays.asList(values()));
        }
        if (role == UI_ADMIN) {
            result.add(UI_USER);
            result.add(UI_GUEST);
        }
        if (role == API_ADMIN) {
            result.add(API_PING);
            result.add(API_GET_TRANSACTION);
            result.add(API_CONFIRM_TRANSACTION);
        }
        return result;
    }

    public int getCode() {
        return code;
    }

    public boolean isMacro() {
        return macro;
    }
}
