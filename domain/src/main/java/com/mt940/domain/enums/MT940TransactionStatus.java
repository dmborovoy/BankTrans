package com.mt940.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MT940TransactionStatus {
    @JsonProperty
    NEW(0, "New unprocessed transaction"),
    @JsonProperty
    PROCESSED(1, "Successfully processed transaction"),
    @JsonProperty
    READ(2, "Instance requested this transaction for processing but didn't respond yet with processing result"),
    @JsonProperty
    DISALLOW_PLATFORM_FEED(3, "Platform feed is not allowed"),
    @JsonProperty
    ERROR_BUSINESS(100, "Root business error"),
    @JsonProperty
    ERROR_BUSINESS_ACCOUNT_WAS_NOT_FOUND(101, "Account was not found"),
    @JsonProperty
    ERROR_BUSINESS_ACCOUNT_IS_CLOSED(102, "Account is closed"),
    @JsonProperty
    ERROR_BUSINESS_ACCOUNT_IS_FORBIDDEN(103, "Account is forbidden for operation"),
    @JsonProperty
    ERROR_BUSINESS_INSUFFICIENT_FUNDS(104, "Account has insufficient funds"),
    @JsonProperty
    ERROR_PROCESSING(1000, "Root processing error"),
    @JsonProperty
    ERROR_PROCESSING_INVALID_TARGET_STATUS(1001, "Target transaction must have 'READ' status. You can update status for transactions that was read before"),
    @JsonProperty
    ERROR_PROCESSING_ID_WAS_NOT_FOUND(1002, "Transaction id was not found in valletta-service"),
    @JsonProperty
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
