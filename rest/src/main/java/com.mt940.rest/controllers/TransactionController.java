package com.mt940.rest.controllers;

import com.mt940.rest.dto.TransactionView;
import com.mt940.rest.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class TransactionController {
    private static final int DEFAULT_PAGE_SIZE = 5;

    @Autowired
    private TransactionService transactionService;


    @PreAuthorize("hasRole(anonymous)")
    @GetMapping(value = "/transaction/{id}")
    public TransactionView getTransactionById(@PathVariable("id") long transactionId) {
        return transactionService.findById(transactionId);
    }

    @PostMapping(value = "/transaction")
    public void addTransaction(@RequestBody TransactionView transaction) {
        transactionService.addTransaction(transaction);
    }

    @PutMapping(value = "/transaction/{id}")
    public void updateTransaction(@PathVariable("id") long transactionId, @RequestBody TransactionView transaction) {
        transaction.setId(transactionId);
        transactionService.updateTransaction(transaction);
    }

    @DeleteMapping(value = "/transaction/{id}")
    public void deleteTransaction(@PathVariable("id") long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    @GetMapping(value = "/transaction/list")
    public Page<TransactionView> listTransactions(@RequestParam(value = "filter", required = false) String filter,
                                                  @PageableDefault(size = DEFAULT_PAGE_SIZE)
                                                          Pageable pageable) {
        return transactionService.listAllByPage(pageable, filter);
    }
}


