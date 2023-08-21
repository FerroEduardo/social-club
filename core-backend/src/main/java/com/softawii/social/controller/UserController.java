package com.softawii.social.controller;

import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("me")
    public Object user(OAuth2AuthenticationToken authentication) throws Exception {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        return service.findByEmail(user.getEmail()).get();
    }
}
