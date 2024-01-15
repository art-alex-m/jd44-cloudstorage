package ru.netology.cloudstorage.webapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.repository.AuthTokenRepository;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.webapp.model.AppAuthToken;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

import java.util.Optional;
import java.util.Random;

@Service
public class AppAuthTokenManager implements AuthTokenManager {
    private final Random random = new Random();
    private final AppAuthTokenProperties tokenProperties;
    private final AuthTokenRepository tokenRepository;

    public AppAuthTokenManager(AppAuthTokenProperties tokenProperties, AuthTokenRepository tokenRepository) {
        this.tokenProperties = tokenProperties;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String createToken(Authentication authentication) {
        String tokenValue = createToken(tokenProperties.getLength());
        tokenRepository.store(
                new AppAuthToken(authentication.getAuthorities(), authentication.getPrincipal(), tokenValue));
        return tokenValue;
    }

    @Override
    public Optional<AuthToken> getToken(String token) {
        return tokenRepository.findByValue(prepareToken(token));
    }

    @Override
    public boolean revokeToken(String token) {
        tokenRepository.delete(prepareToken(token));
        return true;
    }

    public String createToken(int tokenLength) {
        int leftLimit = 48;
        int rightLimit = 122;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(tokenLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String prepareToken(String rawToken) {
        return rawToken != null
                ? rawToken.replace("Bearer ", "")
                : "";
    }
}
