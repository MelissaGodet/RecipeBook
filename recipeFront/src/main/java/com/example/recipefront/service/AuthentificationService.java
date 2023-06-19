package com.example.recipefront.service;
import com.example.recipefront.model.Jwt;
import com.example.recipefront.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {

    private static final String ROOT_URL = "http://localhost:8081/authentication";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Jwt> login(Login login) {
        String url = ROOT_URL + "/login";
        return restTemplate.postForEntity(url, login, Jwt.class);
    }

    public void saveJwtToLocalStorage(String jwt) {
        // Code pour enregistrer le JWT dans le local storage
        // Dépend de votre implémentation spécifique
    }

    public boolean isUserAuthenticated() {
        String decodedToken = decodeJwt();
        if (decodedToken != null) {
            // Vérifier si le token contient les informations d'authentification nécessaires
            // Retourner true si l'utilisateur est authentifié, sinon false
        }
        return false;
    }

    public String getAuthenticatedUserUsername() {
        String decodedToken = decodeJwt();
        if (decodedToken != null) {
            // Extraire le nom d'utilisateur à partir du token JWT
            // Retourner le nom d'utilisateur
        }
        return null;
    }

    public boolean isUserAdmin() {
        String decodedToken = decodeJwt();
        if (decodedToken != null) {
            // Vérifier si le token contient les rôles nécessaires pour l'administrateur
            // Retourner true si l'utilisateur est un administrateur, sinon false
        }
        return false;
    }

    public void logout() {
        // Code pour supprimer le JWT du local storage
        // Dépend de votre implémentation spécifique
    }

    public String decodeJwt() {
        // Récupérer le JWT depuis le local storage
        // Dépend de votre implémentation spécifique
        if (token != null) {
            // Décoder le JWT et extraire les informations nécessaires
            // Retourner le JWT décodé
        } else {
            return null;
        }
    }
}
