package com.mt940.ui.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mt940.ui.domain.json.FieldName;
import com.mt940.ui.domain.json.UISerializable;
import com.mt940.ui.service.serializer.AmountSerializer;

public class StatementDetails implements UISerializable {
    @FieldName("Id")
    Long id;
    @FieldName("Email Id")
    Long messageId;
    @FieldName("File Id")
    Long attachmentId;
    @FieldName("SWIFT Header #1")
    String SWIFTHeader1;
    @FieldName("SWIFT Header #2")
    String SWIFTHeader2;
    @FieldName("SWIFT Header #3")
    String SWIFTHeader3;
    @FieldName("Transaction Reference")
    String transactionReference;
    @FieldName("Related Reference")
    String relatedReference;
    @FieldName("Account Id")
    String accountId;
    @FieldName("Sequence Number")
    String sequenceNumber;
    @FieldName("Statement Number")
    String statementNumber;
    @FieldName("Opening Balance")
    @JsonSerialize(contentUsing = AmountSerializer.class)
    Amount openingBalance;
    @FieldName("Closing Balance")
    @JsonSerialize(contentUsing = AmountSerializer.class)
    Amount closingBalance;
    @FieldName("Entry Order")
    Integer entryOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Amount getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Amount openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Amount getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Amount closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSWIFTHeader1() {
        return SWIFTHeader1;
    }

    public void setSWIFTHeader1(String SWIFTHeader1) {
        this.SWIFTHeader1 = SWIFTHeader1;
    }

    public String getSWIFTHeader2() {
        return SWIFTHeader2;
    }

    public void setSWIFTHeader2(String SWIFTHeader2) {
        this.SWIFTHeader2 = SWIFTHeader2;
    }

    public String getSWIFTHeader3() {
        return SWIFTHeader3;
    }

    public void setSWIFTHeader3(String SWIFTHeader3) {
        this.SWIFTHeader3 = SWIFTHeader3;
    }

    public Integer getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(Integer entryOrder) {
        this.entryOrder = entryOrder;
    }

    public String getRelatedReference() {
        return relatedReference;
    }

    public void setRelatedReference(String relatedReference) {
        this.relatedReference = relatedReference;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "id=" + id +
                ", attachmentId=" + attachmentId +
                ", accountId='" + accountId + '\'' +
                ", sequenceNumber='" + sequenceNumber + '\'' +
                ", statementNumber='" + statementNumber + '\'' +
                ", transactionReference='" + transactionReference + '\'' +
                ", openingBalance=" + openingBalance +
                ", closingBalance=" + closingBalance +
                '}';
    }
}
