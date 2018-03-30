package com.mt940.ui.form;


import java.time.LocalDateTime;

public class TransactionFilterForm extends AbstractFilterForm {
    Long messageId;
    Long fileId;
    Long statementId;
    Integer status;
    Integer instance;
    String fundsCode;
    String infoToAccountOwner;
    String refForAccountOwner;
    String refForBank;
    String transactionDescription;
    LocalDateTime from;
    LocalDateTime to;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    public String getFundsCode() {
        return fundsCode;
    }

    public void setFundsCode(String fundsCode) {
        this.fundsCode = fundsCode;
    }

    public String getInfoToAccountOwner() {
        return infoToAccountOwner;
    }

    public void setInfoToAccountOwner(String infoToAccountOwner) {
        this.infoToAccountOwner = infoToAccountOwner;
    }

    public String getRefForAccountOwner() {
        return refForAccountOwner;
    }

    public void setRefForAccountOwner(String refForAccountOwner) {
        this.refForAccountOwner = refForAccountOwner;
    }

    public String getRefForBank() {
        return refForBank;
    }

    public void setRefForBank(String refForBank) {
        this.refForBank = refForBank;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
