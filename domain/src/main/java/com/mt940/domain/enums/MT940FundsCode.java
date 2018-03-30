package com.mt940.domain.enums;

public enum MT940FundsCode {
    CREDIT("C"),
    DEBIT("D"),
    CREDIT_REVERS("RC"),
    DEBIT_REVERS("RD");

    final String code;

    private MT940FundsCode(String code) {
        this.code = code;
    }

    public static MT940FundsCode findByCode(String code) {
        MT940FundsCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MT940FundsCode value = var1[var3];
            if (value.code.equals(code.trim().toUpperCase())) {
                return value;
            }
        }

        return null;
    }

    public String getCode() {
        return this.code;
    }
}
