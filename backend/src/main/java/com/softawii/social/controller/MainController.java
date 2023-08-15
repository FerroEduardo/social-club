package com.softawii.social.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Hello, world";
    }

    @GetMapping("/login/success")
    public RedirectView loginSuccess() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:5173/");

        return redirectView;
    }

}
