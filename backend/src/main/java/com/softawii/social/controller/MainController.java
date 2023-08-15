package com.softawii.social.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Hello, world";
    }

    @GetMapping("/login")
    public String login() {
        return "aaaaaaaaa";
    }

}
