package com.mt940.rest.controllers;


import com.mt940.domain.permission.BKVUser;
import com.mt940.rest.dto.UserDetailsView;
import com.mt940.rest.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
    @GetMapping(value = "/auth")
    public UserDetailsView getDetailsByCredentials(@RequestHeader(value = "Authorization") String auth) {
        return userService.loadDetailsByCredentials(auth);
    }

    @CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
    @GetMapping(value = "/users/list")
    public List<BKVUser> getUsers(@RequestHeader(value = "Authorization") String auth) {
        return userService.listAll();
    }

}
