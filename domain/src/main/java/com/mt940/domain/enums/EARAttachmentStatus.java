package com.mt940.domain.enums;

public enum EARAttachmentStatus {
    NEW(0),
    PROCESSED(1),
    ERROR(2);

    final int code;

    EARAttachmentStatus(int code) {
        this.code = code;
    }

    public static EARAttachmentStatus findByCode(int code) {
        for (EARAttachmentStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}
