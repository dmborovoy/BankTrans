package com.mt940.dao.jpa;


import com.mt940.domain.permission.BKVUser;

public interface BKVUserDao extends Dao<BKVUser, Long> {

    BKVUser findUser(String login);

    void deleteById(Long id);
}
