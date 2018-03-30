package com.mt940.domain.mt940;


import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;

import java.time.ZonedDateTime;

/**
 * Created by dimas on 3/11/2015.
 */
public class MT940TransactionSearchRequest {
    public Long statementId = null;
    public Long fileId = null;
    public Long messageId = null;
    public ZonedDateTime from = null;
    public ZonedDateTime to = null;
    public MT940FundsCode fundsCode = null;
    public String referenceForAccountOwner = null;
    public String referenceForBank = null;
    public String transactionDescription = null;
    public MT940TransactionStatus status = null;
    public Instance instance = null;
    public String informationToAccountOwner = null;
}
