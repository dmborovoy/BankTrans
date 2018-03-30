package com.mt940.dao.repository;

import com.mt940.domain.permission.BKVUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service("bkvUserRepository")
public interface BKVUserRepository extends PagingAndSortingRepository<BKVUser, Long>, JpaSpecificationExecutor {

    BKVUser findTopByLogin(String login);

}
