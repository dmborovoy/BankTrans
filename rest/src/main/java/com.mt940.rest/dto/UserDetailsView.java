package com.mt940.rest.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

@Getter
@Setter


public class UserDetailsView {
    private String username;
    private boolean isEnabled;
    private ArrayList<? extends GrantedAuthority> authorities;
}
