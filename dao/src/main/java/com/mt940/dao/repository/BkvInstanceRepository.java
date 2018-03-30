package com.mt940.dao.repository;

import com.mt940.domain.permission.BKVInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by vmayorov on 02/06/2015.
 */
@Service("bkvInstanceRepository")
public interface BkvInstanceRepository extends CrudRepository<BKVInstance, Integer> {
    BKVInstance findByCode(String code);
}
