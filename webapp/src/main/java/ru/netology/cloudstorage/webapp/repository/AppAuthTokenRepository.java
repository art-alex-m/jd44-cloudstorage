package ru.netology.cloudstorage.webapp.repository;

import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.repository.AuthTokenRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AppAuthTokenRepository implements AuthTokenRepository {

    private final Map<String, AuthToken> tokenStorageMap = new ConcurrentHashMap<>();

    @Override
    public AuthToken store(AuthToken token) {
        tokenStorageMap.put(token.getValue(), token);
        return token;
    }

    @Override
    public Optional<AuthToken> findByValue(String value) {
        return Optional.ofNullable(tokenStorageMap.get(value));
    }

    @Override
    public void delete(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        tokenStorageMap.remove(value);
    }
}
