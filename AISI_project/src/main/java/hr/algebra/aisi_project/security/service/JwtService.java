package hr.algebra.aisi_project.security.service;


import hr.algebra.aisi_project.security.domain.User;

public interface JwtService {

    boolean authenticate(String token);

    String createJwt(User user);

}
