package ru.netology.cloudstorage.webapp.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.repository.AuthTokenRepository;
import ru.netology.cloudstorage.webapp.model.AppAuthToken;

import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(value = "cloudstorage.auth.token.redis-token", havingValue = "1")
public interface AppAuthTokenRedisRepository
        extends CrudRepository<AppAuthToken, String>, AuthTokenRepository {

    @Override
    default AuthToken store(AuthToken token) {
        save((AppAuthToken) token);
        return token;
    }

    @Override
    default Optional<AuthToken> findByValue(String value) {
        return findById(value).map(v -> v);
    }

    @Override
    default void delete(String value) {
        deleteById(value);
    }
}
