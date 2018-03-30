package com.mt940.dao.jpa;

import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940Transaction;
import com.mt940.domain.mt940.MT940TransactionSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface MT940TransactionDao extends Dao<MT940Transaction, Long> {

    public Page<MT940Transaction> findAllByPage(Pageable pageable);

    public List<MT940Transaction> findByAllNullable(MT940TransactionStatus status, Instance instance, MT940FundsCode fundsCode, ZonedDateTime from, ZonedDateTime to, String infoToAccountOwner, String referenceForAccountOwner, String referenceForBank, String transactionDescription, Long emailId, Long fileId, Long statementId);

    public Page<MT940Transaction> findByAllNullable(MT940TransactionStatus status, Instance instance, MT940FundsCode fundsCode, ZonedDateTime from, ZonedDateTime to, String infoToAccountOwner, String referenceForAccountOwner, String referenceForBank, String transactionDescription, Long emailId, Long fileId, Long statementId, Pageable pageable);

    public List<MT940Transaction> findByAllNullable(final MT940TransactionSearchRequest request);

    public Page<MT940Transaction> findByAllNullable(final MT940TransactionSearchRequest request, Pageable pageable);


    List<MT940Transaction> getTransactionListByDateTransactionTypeInstanceAndStatus(Instance instance, ZonedDateTime fromDate, ZonedDateTime toDate, MT940FundsCode fundsCode, MT940TransactionStatus status);

    List<MT940Transaction> getOrphanTransactionList(ZonedDateTime fromDate, MT940FundsCode fundsCode, MT940TransactionStatus status);

    List<MT940Transaction> getReadTransactionList(MT940FundsCode fundsCode, MT940TransactionStatus status);

    List<MT940Transaction> getErrorTransactionList(MT940FundsCode fundsCode, MT940TransactionStatus status);

    List<MT940Transaction> getUnknownInstanceTransactionList(MT940FundsCode fundsCode);

    void updateStatus(MT940Transaction transaction, MT940TransactionStatus status, String errorDescription, Instance instance);

    void updateStatus(Long id, MT940TransactionStatus status, String errorDescription, Instance instance);

}
