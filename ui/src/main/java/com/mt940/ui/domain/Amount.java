package com.mt940.ui.domain;

import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

public class Amount {
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    BigDecimal amount;
    String currency;

    public Amount(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Amount() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
