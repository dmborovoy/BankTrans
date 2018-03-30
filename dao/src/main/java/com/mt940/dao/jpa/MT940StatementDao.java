package com.mt940.dao.jpa;

import com.mt940.domain.mt940.MT940Statement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MT940StatementDao extends Dao<MT940Statement, Long> {

    public Page<MT940Statement> findAllByPage(Pageable pageable);

    public List<MT940Statement> findByAllNullable(String accountId, String relatedReference, String transactionReference, Long messageId, Long fileId);

    public Page<MT940Statement> findByAllNullable(String accountId, String relatedReference, String transactionReference, Long messageId, Long fileId, Pageable pageable);

}
