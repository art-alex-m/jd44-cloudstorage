package ru.netology.cloudstorage.contracts.auth.repository;


import ru.netology.cloudstorage.contracts.auth.model.AuthToken;

import java.util.Optional;

public interface AuthTokenRepository {
    AuthToken store(AuthToken token);

    Optional<AuthToken> findByValue(String value);

    void delete(String value);
}
