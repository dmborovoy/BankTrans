package com.mt940.domain.enums;

public enum MT940TransactionStatus {
    NEW(0, "New unprocessed transaction"),
    PROCESSED(1, "Successfully processed transaction"),
    READ(2, "Instance requested this transaction for processing but didn't respond yet with processing result"),
    DISALLOW_PLATFORM_FEED(3, "Platform feed is not allowed"),
    ERROR_BUSINESS(100, "Root business error"),
    ERROR_BUSINESS_ACCOUNT_WAS_NOT_FOUND(101, "Account was not found"),
    ERROR_BUSINESS_ACCOUNT_IS_CLOSED(102, "Account is closed"),
    ERROR_BUSINESS_ACCOUNT_IS_FORBIDDEN(103, "Account is forbidden for operation"),
    ERROR_BUSINESS_INSUFFICIENT_FUNDS(104, "Account has insufficient funds"),
    ERROR_PROCESSING(1000, "Root processing error"),
    ERROR_PROCESSING_INVALID_TARGET_STATUS(1001, "Target transaction must have 'READ' status. You can update status for transactions that was read before"),
    ERROR_PROCESSING_ID_WAS_NOT_FOUND(1002, "Transaction id was not found in valletta-service"),
    ERROR_PROCESSING_INVALID_INCOME_STATUS(1003, "Status of processed transaction is invalid");

    final int code;
    final String description;

    private MT940TransactionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MT940TransactionStatus findByCode(int code) {
        MT940TransactionStatus[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MT940TransactionStatus value = var1[var3];
            if (value.code == code) {
                return value;
            }
        }

        return null;
    }

    public int getCode() {
        return this.code;
    }
}
