package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.repository.AuthTokenRepository;
import ru.netology.cloudstorage.webapp.model.AppAuthToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppAuthTokenMapRepositoryTest {

    @Test
    void store() {
        String tokenValue = "some-test-value";
        AuthToken authToken = new AppAuthToken(List.of(), "som@example.com", tokenValue);
        AuthTokenRepository sut = new AppAuthTokenMapRepository();

        AuthToken result = sut.store(authToken);
        Optional<AuthToken> testResult = sut.findByValue(tokenValue);

        assertFalse(testResult.isEmpty());
        assertEquals(authToken, testResult.get());
        assertEquals(authToken, result);
    }

    @Test
    void findByValue() {
        String tokenValue = "some-test-value";
        AuthToken authToken = new AppAuthToken(List.of(), "som@example.com", tokenValue);
        AuthTokenRepository sut = new AppAuthTokenMapRepository();
        sut.store(authToken);

        Optional<AuthToken> result = sut.findByValue(tokenValue);

        assertFalse(result.isEmpty());
        assertEquals(authToken, result.get());
    }

    @Test
    void delete() {
        String tokenValue = "some-test-value";
        AuthToken authToken = new AppAuthToken(List.of(), "som@example.com", tokenValue);
        AuthTokenRepository sut = new AppAuthTokenMapRepository();
        sut.store(authToken);

        sut.delete(tokenValue);
        Optional<AuthToken> result = sut.findByValue(tokenValue);

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a123456", ""})
    @NullSource
    void deleteWithNoExceptionOnNullKey(String tokenValue) {
        AuthTokenRepository sut = new AppAuthTokenMapRepository();

        Executable result = () -> sut.delete(tokenValue);

        assertDoesNotThrow(result);
    }
}
