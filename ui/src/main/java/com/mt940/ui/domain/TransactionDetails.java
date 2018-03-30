package com.mt940.ui.domain;

import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.ui.domain.json.FieldName;
import com.mt940.ui.domain.json.UISerializable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;

public class TransactionDetails implements UISerializable {
    @FieldName("Id")
    Long id;
    @FieldName("Email Id")
    Long messageId;
    @FieldName("File Id")
    Long fileId;
    @FieldName("Statement Id")
    Long statementId;
    @FieldName("Date")
    LocalDate date;
    @FieldName("Entry Date")
    LocalDate entryDate;
    @FieldName("Funds Code")
    MT940FundsCode fundsCode;
    @FieldName("Amount")
    Amount amount;
    @FieldName("Swift Code")
    String swiftCode;
    @FieldName("Reference For Account Owner")
    String referenceForAccountOwner;
    @FieldName("Reference For Bank")
    String referenceForBank;
    @FieldName("Transaction Description")
    String transactionDescription;
    @FieldName(value = "Status", isEditable = true)
    MT940TransactionStatus status;
    @FieldName("Error Description")
    String errorDescription;
    @FieldName(value = "Instance", isEditable = true)
    Instance instance;
    @FieldName("Entry Order")
    Integer entryOrder;
    @FieldName("Raw Transaction")
    String rawTransaction;
    @FieldName("Information to Account Owner")
    String informationToAccountOwner;

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getReferenceForBank() {
        return referenceForBank;
    }

    public void setReferenceForBank(String referenceForBank) {
        this.referenceForBank = referenceForBank;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Integer getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(Integer entryOrder) {
        this.entryOrder = entryOrder;
    }

    public String getRawTransaction() {
        return rawTransaction;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }

    public String getInformationToAccountOwner() {
        return informationToAccountOwner;
    }

    public void setInformationToAccountOwner(String informationToAccountOwner) {
        this.informationToAccountOwner = informationToAccountOwner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
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

    @PreAuthorize("hasRole('UI_ADMIN')")
    public MT940TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(MT940TransactionStatus status) {
        this.status = status;
    }

    public String getReferenceForAccountOwner() {
        return referenceForAccountOwner;
    }

    public void setReferenceForAccountOwner(String referenceForAccountOwner) {
        this.referenceForAccountOwner = referenceForAccountOwner;
    }

    @PreAuthorize("hasRole('UI_ADMIN')")
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

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", statementId=" + statementId +
                ", amount=" + amount +
                ", date=" + date +
                ", transactionDescription='" + transactionDescription + '\'' +
                ", status=" + status +
                '}';
    }
}
