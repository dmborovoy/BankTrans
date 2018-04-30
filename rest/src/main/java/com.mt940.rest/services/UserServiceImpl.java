package com.mt940.rest.services;

import com.mt940.dao.jpa.BKVUserDao;
import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.permission.BKVUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private BKVUserDao userDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        BKVUser user = userDao.findUser(username);
        log.info("BKVUser: {}", user);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(BKVUser user, List<GrantedAuthority> authorities) {
        return new User(user.getLogin(), user.getPassword(), !user.isDisabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<BKVRoles> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        for (BKVRoles userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}
