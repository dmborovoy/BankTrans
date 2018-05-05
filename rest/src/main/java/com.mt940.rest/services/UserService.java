package com.mt940.rest.services;

import com.mt940.rest.dto.UserDetailsView;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserService extends UserDetailsService {

    @Transactional
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, BadCredentialsException;

    @Transactional
    UserDetails loadUserByCredentials(final String auth);

    @Transactional
    UserDetailsView loadDetailsByCredentials(final String auth);
}
