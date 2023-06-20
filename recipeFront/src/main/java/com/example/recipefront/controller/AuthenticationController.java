package com.example.recipefront.controller;

import com.example.recipefront.model.Authority;
import com.example.recipefront.model.Jwt;
import com.example.recipefront.model.Login;
import com.example.recipefront.service.AuthenticationService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authentificationService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) {
        Login login = new Login(username, password);
        Jwt jwt =authentificationService.send(login);
        Authority[] authorities = authentificationService.loginAuthorities(login);
        session.setAttribute("Role", authorities[0].getName());
        session.setAttribute("Authorization", jwt.getJwt());
        return "redirect:/recipe";
    }
}
