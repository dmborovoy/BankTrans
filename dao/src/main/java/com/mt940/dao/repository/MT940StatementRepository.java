package com.mt940.dao.repository;

import com.mt940.domain.mt940.MT940Statement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service("mt940StatementRepository")
public interface MT940StatementRepository extends PagingAndSortingRepository<MT940Statement, Long>, JpaSpecificationExecutor {

}
