package com.mt940.rest.dto;

import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter

public class TransactionView {
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
    private Long statementId;  //statement id
    private MT940TransactionStatus status;
    private String errorDescription;
    private Instance instance;
    private int entryOrder;
    private String informationToAccountOwner;
}
