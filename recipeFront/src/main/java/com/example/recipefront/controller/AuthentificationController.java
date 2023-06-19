package com.example.recipefront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthentificationController {

    @GetMapping("authentification/login")
    public String showLoginPage() {
        return "login";
    }

}
