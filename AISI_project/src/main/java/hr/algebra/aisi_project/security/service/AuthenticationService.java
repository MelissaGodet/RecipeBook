package hr.algebra.aisi_project.security.service;



import hr.algebra.aisi_project.security.command.LoginCommand;
import hr.algebra.aisi_project.security.domain.Authority;
import hr.algebra.aisi_project.security.dto.LoginDTO;

import java.util.Optional;
import java.util.Set;

public interface AuthenticationService {

    Optional<LoginDTO> login(LoginCommand command);

    Set<Authority> loginAuthorities(LoginCommand command);
}
