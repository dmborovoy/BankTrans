package com.mt940.dao.repository;

import com.mt940.domain.permission.BKVRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by vmayorov on 27/05/2015.
 */
@Service("bkvRoleRepository")
public interface BkvRoleRepository extends CrudRepository<BKVRole, Integer> {
}
