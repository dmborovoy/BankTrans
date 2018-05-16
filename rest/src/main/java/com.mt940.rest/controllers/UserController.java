package com.mt940.rest.controllers;


import com.mt940.rest.dto.UserDetailsView;
import com.mt940.rest.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
    @GetMapping(value = "/users")
    public UserDetailsView getDetailsByCredentials(@RequestHeader(value = "Authorization") String auth) {
        return userService.loadDetailsByCredentials(auth);

    }

}
