package com.mt940.domain.enums;

public enum MT940ResultCode {
    UNKNOWN(-1),
    OK(0),
    COMMUNICATION_ERROR(1),
    INTERNAL_ERROR(2),
    MANDATORY_FIELD_IS_MISSING(3),
    UNKNOWN_REQUEST_ENTITY(4),
    SERVICE_IS_NOT_REGISTERED(5),
    INVALID_FIELD_VALUE(6),
    TRANSACTION_PROCESSING_ERROR(7),
    INVALID_CREDENTIALS(8);

    final int code;

    private MT940ResultCode(int code) {
        this.code = code;
    }

    public static MT940ResultCode findByCode(int code) {
        MT940ResultCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MT940ResultCode value = var1[var3];
            if (value.code == code) {
                return value;
            }
        }

        return UNKNOWN;
    }

    public int getCode() {
        return this.code;
    }
}