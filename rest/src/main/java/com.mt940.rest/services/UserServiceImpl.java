package com.mt940.rest.services;

import com.mt940.dao.jpa.BKVUserDao;
import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.permission.BKVUser;
import com.mt940.rest.dto.UserDetailsView;
import com.sun.org.apache.xerces.internal.impl.dv.xs.UnionDV;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BKVUserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Qualifier("userDetailsMapperFacade")
    @Autowired
    MapperFacade mapperFacade;

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

    public UserDetails loadUserByCredentials(String auth) throws BadCredentialsException {
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            final String[] values = credentials.split(":", 2);

            BKVUser user = userDao.findUser(values[0]);

            log.info("BKVUser: {}", user);
            if (!passwordEncoder.matches(values[1], user.getPassword()))
                log.info("Wrong password: {} for BKVUser: {}", values[1], user.getLogin());
            List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());

            return buildUserForAuthentication(user, authorities);
        } else throw new BadCredentialsException("Basic authorization required");
    }

    public UserDetailsView loadDetailsByCredentials(String auth) {
        UserDetails userDetails = loadUserByCredentials(auth);
        UserDetailsView details = mapperFacade.map(userDetails, UserDetailsView.class);
        details.setAuthorities(new ArrayList<>(userDetails.getAuthorities()));
        return details;

    }
}
