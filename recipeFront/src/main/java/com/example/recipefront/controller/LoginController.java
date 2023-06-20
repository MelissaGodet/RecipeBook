package com.example.recipefront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping
    public String index() {
        return "login";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("Role");
        session.removeAttribute("Authorization");
        return "redirect:login";
    }


    @GetMapping("error")
    public String error(HttpSession session) {
        session.removeAttribute("Role");
        session.removeAttribute("Authorization");
        return "error";
    }

}
