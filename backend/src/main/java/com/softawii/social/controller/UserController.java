package com.softawii.social.controller;

import com.softawii.social.model.User;
import com.softawii.social.repository.UserRepository;
import com.softawii.social.security.UserPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("me")
    public Object user(OAuth2AuthenticationToken authentication) throws Exception {
        UserPrincipal user   = (UserPrincipal) authentication.getPrincipal();
        User          userDb = userRepository.findByEmail(user.getEmail()).get();

        return userDb;
    }
}
