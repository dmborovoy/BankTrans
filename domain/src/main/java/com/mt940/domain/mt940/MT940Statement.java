package com.mt940.domain.mt940;

import com.mt940.domain.EARAttachment;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.SortedSet;

@Entity
@Table(schema = "bkv", name = "mt940_statement")
public class MT940Statement implements Comparable<MT940Statement> {

    private Long id;
    private String SWIFTHeader1;
    private String SWIFTHeader2;
    private String SWIFTHeader3;
    private String transactionReference;
    private String relatedReference;
    private String accountId;
    private String statementNumber;
    private String sequenceNumber;
    private MT940Balance openingBalance;
    private SortedSet<MT940Transaction> transactionSet;
    private MT940Balance closingBalance;
    private int entryOrder;

    private EARAttachment settlementFile;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "settlement_file_id", referencedColumnName = "id")
    public EARAttachment getSettlementFile() {
        return settlementFile;
    }

    public void setSettlementFile(EARAttachment settlementFile) {
        this.settlementFile = settlementFile;
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

    @Column(name = "swift_header1")
    public String getSWIFTHeader1() {
        return SWIFTHeader1;
    }

    public void setSWIFTHeader1(String SWIFTHeader1) {
        this.SWIFTHeader1 = SWIFTHeader1;
    }

    @Column(name = "swift_header2")
    public String getSWIFTHeader2() {
        return SWIFTHeader2;
    }

    public void setSWIFTHeader2(String SWIFTHeader2) {
        this.SWIFTHeader2 = SWIFTHeader2;
    }

    @Column(name = "swift_header3")
    public String getSWIFTHeader3() {
        return SWIFTHeader3;
    }

    public void setSWIFTHeader3(String SWIFTHeader3) {
        this.SWIFTHeader3 = SWIFTHeader3;
    }

    @Column(name = "transaction_reference")
    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @Column(name = "related_reference")
    public String getRelatedReference() {
        return relatedReference;
    }

    public void setRelatedReference(String relatedReference) {
        this.relatedReference = relatedReference;
    }

    @Column(name = "account_id")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Column(name = "statement_number")
    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    @Column(name = "sequence_number")
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "opening_balance_id")
    public MT940Balance getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(MT940Balance openingBalance) {
        this.openingBalance = openingBalance;
    }

    @JoinColumn(name = "statement_id", referencedColumnName = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Sort(type = SortType.NATURAL)
    public SortedSet<MT940Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(SortedSet<MT940Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "closing_balance_id")
    public MT940Balance getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(MT940Balance closingBalance) {
        this.closingBalance = closingBalance;
    }

    //Note: this class has a natural ordering that is inconsistent with equals
    @Override
    public int compareTo(MT940Statement other) {
        int result = this.getEntryOrder() - other.getEntryOrder();
        return result == 0 ? -1 : result;//if transactions are equal by entryDate just make new element less
    }

    @Override
    public String toString() {
        return "MT940Statement{" +
                "id=" + id +
                ", SWIFTHeader1='" + SWIFTHeader1 + '\'' +
                ", SWIFTHeader2='" + SWIFTHeader2 + '\'' +
                ", SWIFTHeader3='" + SWIFTHeader3 + '\'' +
                ", transactionReference='" + transactionReference + '\'' +
                ", relatedReference='" + relatedReference + '\'' +
                ", accountId='" + accountId + '\'' +
                ", statementNumber='" + statementNumber + '\'' +
                ", sequenceNumber='" + sequenceNumber + '\'' +
                ", openingBalance=" + openingBalance +
                ", closingBalance=" + closingBalance +
                "'}@" + Integer.toHexString(hashCode());
    }

    public String toDetailedString() {
        return "MT940Statement{" + "\r\n" +
                "id=" + id + "\r\n" +
                "SWIFTHeader1='" + SWIFTHeader1 + '\'' + "\r\n" +
                "SWIFTHeader2='" + SWIFTHeader2 + '\'' + "\r\n" +
                "SWIFTHeader3='" + SWIFTHeader3 + '\'' + "\r\n" +
                "transactionReference='" + transactionReference + '\'' + "\r\n" +
                "accountId='" + accountId + '\'' + "\r\n" +
                "statementNumber='" + statementNumber + '\'' + "\r\n" +
                "openingBalance=" + "\r\n\t" + openingBalance + "\r\n" +
                "transactionSet=" + transactionSet + "\r\n" +
                "closingBalance=" + "\r\n\t" + closingBalance + "\r\n" +
                "}@" + Integer.toHexString(hashCode());
    }


}
