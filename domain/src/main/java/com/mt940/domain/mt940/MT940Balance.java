package com.mt940.domain.mt940;

import com.mt940.domain.enums.MT940BalanceType;
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
@Table(schema = "bkv", name = "mt940_balance")
@TypeDef(name = "PersistentEnum", typeClass = PersistentEnum.class)
public class MT940Balance {

    private Long id;
    private MT940BalanceType balanceType;
    private ZonedDateTime date;
    private String currency;
    private BigDecimal amount;

    public MT940Balance() {
    }

    public MT940Balance(MT940BalanceType balanceType, ZonedDateTime date, String currency, BigDecimal amount) {
        this.balanceType = balanceType;
        this.date = date;
        this.currency = currency;
        this.amount = amount;
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

    @Column(name = "balance_type")
    @Type(type = "PersistentEnum", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.mt940.domain.enums.MT940BalanceType")
            , @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "getCode")
            , @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "findByCode")})
    public MT940BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(MT940BalanceType balanceType) {
        this.balanceType = balanceType;
    }

    @Column(name = "date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MT940Balance{" +
                "id=" + id +
                ", balanceType=" + balanceType +
                ", date=" + date +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                "'}@" + Integer.toHexString(hashCode());
    }
}
