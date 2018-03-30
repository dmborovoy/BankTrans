package com.mt940.ui.domain;


import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    Long id;
    Long messageId;
    Long fileId;
    Long statementId;
    BigDecimal amount;
    String currency;
    LocalDate date;
    String transactionDescription;
    String referenceForAccountOwner;
    Instance instance;
    MT940FundsCode fundsCode;
    MT940TransactionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getReferenceForAccountOwner() {
        return referenceForAccountOwner;
    }

    public void setReferenceForAccountOwner(String referenceForAccountOwner) {
        this.referenceForAccountOwner = referenceForAccountOwner;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public MT940FundsCode getFundsCode() {
        return fundsCode;
    }

    public void setFundsCode(MT940FundsCode fundsCode) {
        this.fundsCode = fundsCode;
    }

    public MT940TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(MT940TransactionStatus status) {
        this.status = status;
    }
}
