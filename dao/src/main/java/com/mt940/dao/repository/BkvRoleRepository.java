package com.mt940.dao.repository;

import com.mt940.domain.permission.BKVRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("bkvRoleRepository")
public interface BkvRoleRepository extends CrudRepository<BKVRole, Integer> {
}
