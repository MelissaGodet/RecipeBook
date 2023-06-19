package hr.algebra.aisi_project.security.controller;


import hr.algebra.aisi_project.security.command.LoginCommand;
import hr.algebra.aisi_project.security.dto.LoginDTO;
import hr.algebra.aisi_project.security.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("authentication")
@CrossOrigin(origins = "http://localhost:8086")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("login")
    public LoginDTO login(@Valid @RequestBody final LoginCommand command) {
        return authenticationService.login(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

}
