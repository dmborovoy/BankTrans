package com.mt940.dao.repository;

import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mt940TransactionRepository")
public interface MT940TransactionRepository extends PagingAndSortingRepository<MT940Transaction, Long>, JpaSpecificationExecutor {

    List<MT940Transaction> findByFundsCodeAndStatusGreaterThan(MT940FundsCode fundsCode, MT940TransactionStatus status);
}
