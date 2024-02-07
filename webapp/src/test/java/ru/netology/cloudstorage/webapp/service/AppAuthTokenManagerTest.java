package ru.netology.cloudstorage.webapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.repository.AuthTokenRepository;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;
import ru.netology.cloudstorage.webapp.model.AppAuthToken;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AppAuthTokenManagerTest {
    private final AuthenticationTestFactory authenticationTestFactory = new AuthenticationTestFactory();

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private AppAuthTokenProperties appAuthTokenProperties;

    @Test
    void createToken() {
        int length = 48;
        Authentication authentication = authenticationTestFactory.createAuthentication();
        Mockito.when(appAuthTokenProperties.getLength()).thenReturn(length);
        AuthTokenManager sut = new AppAuthTokenManager(appAuthTokenProperties, authTokenRepository);
        ArgumentCaptor<AuthToken> authTokenCaptor = ArgumentCaptor.forClass(AuthToken.class);

        String result = sut.createToken(authentication);

        assertNotNull(result);
        assertEquals(length, result.length());
        Mockito.verify(authTokenRepository, Mockito.times(1)).store(authTokenCaptor.capture());
        Mockito.verifyNoMoreInteractions(authTokenRepository);
        AuthToken resultToken = authTokenCaptor.getValue();
        assertNotNull(resultToken);
        assertEquals(authentication.getPrincipal(), resultToken.getPrincipal());
        assertEquals(authentication.getAuthorities(), resultToken.getAuthorities());
        assertEquals(result, resultToken.getValue());
        Mockito.verify(appAuthTokenProperties, Mockito.times(1)).getLength();
        Mockito.verifyNoMoreInteractions(appAuthTokenProperties);
    }

    @Test
    void createTokenValue() {
        int length = 32;
        AppAuthTokenManager sut = new AppAuthTokenManager(appAuthTokenProperties, authTokenRepository);

        String result = sut.createToken(length);

        assertNotNull(result);
        assertEquals(length, result.length());
        Mockito.verifyNoInteractions(authTokenRepository);
        Mockito.verifyNoInteractions(appAuthTokenProperties);
    }

    @Test
    void getToken() {
        Authentication authentication = authenticationTestFactory.createAuthentication();
        String testTokenValue = authenticationTestFactory.getTestTokenValue();
        AuthToken expectedToken = new AppAuthToken(authentication.getAuthorities(), authentication.getPrincipal(),
                testTokenValue);
        Mockito.when(authTokenRepository.findByValue(testTokenValue)).thenReturn(Optional.of(expectedToken));
        AuthTokenManager sut = new AppAuthTokenManager(appAuthTokenProperties, authTokenRepository);

        Optional<AuthToken> result = sut.getToken(testTokenValue);

        assertFalse(result.isEmpty());
        assertEquals(expectedToken.getValue(), result.get().getValue());
        assertEquals(authentication.getPrincipal(), result.get().getPrincipal());
        assertIterableEquals(authentication.getAuthorities(), result.get().getAuthorities());
        Mockito.verifyNoInteractions(appAuthTokenProperties);
    }

    @Test
    void revokeToken() {
        String testTokenValue = authenticationTestFactory.getTestTokenValue();
        AuthTokenManager sut = new AppAuthTokenManager(appAuthTokenProperties, authTokenRepository);

        sut.revokeToken(testTokenValue);

        Mockito.verify(authTokenRepository, Mockito.times(1)).delete(testTokenValue);
        Mockito.verifyNoMoreInteractions(authTokenRepository);
        Mockito.verifyNoInteractions(appAuthTokenProperties);
    }

    @ParameterizedTest
    @CsvSource({"Bearer A123456,A123456", "B987654, B987654", ",''", "'',''"})
    void prepareToken(String rawToken, String expectedPreparedToken) {
        AppAuthTokenManager sut = new AppAuthTokenManager(appAuthTokenProperties, authTokenRepository);

        String result = sut.prepareToken(rawToken);

        assertEquals(expectedPreparedToken, result);
        Mockito.verifyNoInteractions(appAuthTokenProperties);
        Mockito.verifyNoInteractions(authTokenRepository);
    }
}
