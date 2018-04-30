package com.mt940.rest.security;

import com.mt940.rest.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static String REALM = "Authentication required";

    @Autowired
    private UserServiceImpl userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        //authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(15);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/transaction/**").permitAll()
                .antMatchers(HttpMethod.GET,"/transaction/**").permitAll()
                .antMatchers(HttpMethod.POST,"/transaction/").access("hasAnyRole('UI_USER', 'UI_ADMIN')")
                .antMatchers(HttpMethod.PUT,"/transaction/").access("hasRole('UI_ADMIN')")
                .antMatchers(HttpMethod.DELETE,"/transaction/**").access("hasAnyRole('UI_USER', 'UI_ADMIN')")
                .antMatchers(HttpMethod.GET,"/transaction/list/**").permitAll()
                .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }

    @Bean
    public UserServiceImpl getUserService() {
        return new UserServiceImpl();
    }


}
