package com.mt940.domain.mt940;

import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jadira.usertype.corejava.PersistentEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by dimas on 25.11.2014.
 */
@Entity
@Table(schema = "bkv", name = "mt940_transaction")
@TypeDef(name = "PersistentEnum", typeClass = PersistentEnum.class)
public class MT940Transaction implements Comparable<MT940Transaction> {

    private Long id;
    private ZonedDateTime date;
    private ZonedDateTime entryDate;
    private MT940FundsCode fundsCode;
    private BigDecimal amount;
    private String currency;//artificial field taken from opening balance
    private String swiftCode;
    private String referenceForAccountOwner;
    private String referenceForBank;
    private String transactionDescription;
    private MT940Statement statement;
    private MT940TransactionStatus status = MT940TransactionStatus.NEW;
    private String errorDescription;
    private Instance instance = Instance.UNKNOWN;

    private int entryOrder;

    private String rawTransaction;
    private String informationToAccountOwner;

    public MT940Transaction() {
    }

    public MT940Transaction(ZonedDateTime date, ZonedDateTime entryDate, MT940FundsCode fundsCode, BigDecimal amount, String swiftCode, String referenceForAccountOwner, String referenceForBank, String transactionDescription, String informationToAccountOwner) {
        this.date = date;
        this.entryDate = entryDate;
        this.fundsCode = fundsCode;
        this.amount = amount;
        this.swiftCode = swiftCode;
        this.referenceForAccountOwner = referenceForAccountOwner;
        this.referenceForBank = referenceForBank;
        this.transactionDescription = transactionDescription;
        this.informationToAccountOwner = informationToAccountOwner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "entry_order")
    public int getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(int entryOrder) {
        this.entryOrder = entryOrder;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "statement_id", referencedColumnName = "id")
    public MT940Statement getStatement() {
        return statement;
    }

    public void setStatement(MT940Statement statement) {
        this.statement = statement;
    }

    @Column(name = "date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Column(name = "entry_date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    @Column(name = "funds_code")
    @Type(type = "PersistentEnum", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.mt940.domain.enums.MT940FundsCode")
            , @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCode")
            , @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "findByCode")})
    public MT940FundsCode getFundsCode() {
        return fundsCode;
    }

    public void setFundsCode(MT940FundsCode fundsCode) {
        this.fundsCode = fundsCode;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "swift_code")
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Column(name = "reference_for_account_owner")
    public String getReferenceForAccountOwner() {
        return referenceForAccountOwner;
    }

    public void setReferenceForAccountOwner(String referenceForAccountOwner) {
        this.referenceForAccountOwner = referenceForAccountOwner;
    }

    @Column(name = "reference_for_bank")
    public String getReferenceForBank() {
        return referenceForBank;
    }

    public void setReferenceForBank(String referenceForBank) {
        this.referenceForBank = referenceForBank;
    }

    @Column(name = "transaction_description")
    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    @Transient
    public String getRawTransaction() {
        return rawTransaction;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }

    @Column(name = "info_to_account_owner")
    public String getInformationToAccountOwner() {
        return informationToAccountOwner;
    }

    public void setInformationToAccountOwner(String informationToAccountOwner) {
        this.informationToAccountOwner = informationToAccountOwner;
    }

    @Column(name = "status")
    @Type(type = "PersistentEnum", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.mt940.domain.enums.MT940TransactionStatus")
            , @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCode")
            , @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "findByCode")})
    public MT940TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(MT940TransactionStatus status) {
        this.status = status;
    }

    @Column(name = "error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Column(name = "instance")
    @Type(type = "PersistentEnum", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.mt940.domain.enums.Instance")
            , @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCode")
            , @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "findByCode")})
    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "MT940Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", entryDate=" + entryDate +
                ", fundsCode=" + fundsCode +
                ", amount=" + amount +
                ", currency=" + currency +
                ", swiftCode='" + swiftCode + '\'' +
                ", referenceForAccountOwner='" + referenceForAccountOwner + '\'' +
                ", referenceForBank='" + referenceForBank + '\'' +
                ", transactionDescription='" + transactionDescription + '\'' +
                ", informationToAccountOwner='" + informationToAccountOwner + '\'' +
                ", status='" + status + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", instance='" + instance + '\'' +
                "'}@" + Integer.toHexString(hashCode());
    }

    //Note: this class has a natural ordering that is inconsistent with equals
    //We need to compare by entryOrder to sort transactions in the way they were appeared in a file but not to throw them away due Set interface
    @Override
    public int compareTo(MT940Transaction other) {
        int result = this.getEntryOrder() - other.getEntryOrder();
        return result == 0 ? -1 : result;//if transactions are equal by entryDate just make new element less

    }
}
