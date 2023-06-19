package com.example.recipefront.controller;

import com.example.recipefront.model.Jwt;
import com.example.recipefront.model.Login;
import com.example.recipefront.service.AuthentificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("authentication")
public class AuthentificationController {

    @Autowired
    private AuthentificationService authentificationService;

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
        session.setAttribute("Authorization", jwt.getJwt());
        return "redirect:/recipe";
    }
}
