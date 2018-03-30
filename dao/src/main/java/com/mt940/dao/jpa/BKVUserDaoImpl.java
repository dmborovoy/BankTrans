package com.mt940.dao.jpa;

import com.mt940.dao.repository.BKVUserRepository;
import com.mt940.domain.permission.BKVUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("bkvUserDaoImpl")
public class BKVUserDaoImpl extends AbstractDao<BKVUser, Long> implements BKVUserDao {

    @Autowired
    @Qualifier("bkvUserRepository")
    private BKVUserRepository bkvUserRepository;

    @Override
    public Class<BKVUser> getEntityClass() {
        return BKVUser.class;
    }


    @Override
    public BKVUser save(BKVUser entity) {
        return bkvUserRepository.save(entity);
    }

    @Override
    public BKVUser findUser(String login) {
        return bkvUserRepository.findTopByLogin(login);
    }

    @Override
    public void deleteById(Long id) {
        bkvUserRepository.delete(id);
    }
}
