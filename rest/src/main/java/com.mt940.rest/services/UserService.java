package com.mt940.rest.services;

import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.permission.BKVUser;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    @Transactional
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException;

    SecurityProperties.User buildUserForAuthentication(BKVUser user, List<GrantedAuthority> authorities);

    List<GrantedAuthority> buildUserAuthority(Set<BKVRoles> userRoles);
}
