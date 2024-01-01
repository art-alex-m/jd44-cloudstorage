package ru.netology.cloudstorage.contracts.auth.service;

import org.springframework.security.core.Authentication;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;

import java.util.Optional;


public interface AuthTokenManager {
    String createToken(Authentication authentication);

    Optional<AuthToken> getToken(String token);

    void revokeToken(String token);
}
