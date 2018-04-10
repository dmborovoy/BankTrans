package com.mt940.dao.repository;

import com.mt940.domain.EARMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("earMessageRepository")
public interface EARMessageRepository extends PagingAndSortingRepository<EARMessage, Long>, JpaSpecificationExecutor {

    @Query("SELECT DISTINCT e.from FROM EARMessage e")
    List<String> findDistinctSenders();

}
