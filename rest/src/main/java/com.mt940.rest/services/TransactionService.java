package com.mt940.rest.services;


import com.mt940.rest.dto.TransactionView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionView findById(long transactionId);

    void addTransaction(TransactionView transaction);

    void updateTransaction(TransactionView transaction);

    void deleteTransaction(long transactionId);

    Page<TransactionView> listAllByPage(Pageable pageable, String filter);
}
