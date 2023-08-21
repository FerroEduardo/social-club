package com.softawii.social.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MainController {

    @Value("${softawii.front.url}")
    private String frontUrl;

    @GetMapping("/")
    public String home() {
        return "Hello, world";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/login/success")
    public RedirectView loginSuccess() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(this.frontUrl + "/?login=success");

        return redirectView;
    }

    @GetMapping("/logout/success")
    public RedirectView logoutSuccess() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(this.frontUrl + "/?logout=success");

        return redirectView;
    }

}
