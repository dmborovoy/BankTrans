package com.mt940.domain.enums;

public enum MT940BalanceType {
    CREDIT("C"),
    DEBIT("D");

    final String code;

    private MT940BalanceType(String code) {
        this.code = code;
    }

    public static MT940BalanceType findByCode(String code) {
        MT940BalanceType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MT940BalanceType value = var1[var3];
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