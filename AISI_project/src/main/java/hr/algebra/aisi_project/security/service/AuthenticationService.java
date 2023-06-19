package hr.algebra.aisi_project.security.service;



import hr.algebra.aisi_project.security.command.LoginCommand;
import hr.algebra.aisi_project.security.dto.LoginDTO;

import java.util.Optional;

public interface AuthenticationService {

    Optional<LoginDTO> login(LoginCommand command);

}
