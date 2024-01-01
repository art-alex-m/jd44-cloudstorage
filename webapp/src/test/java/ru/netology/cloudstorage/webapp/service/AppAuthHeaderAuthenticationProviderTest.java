package ru.netology.cloudstorage.webapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.webapp.factoy.AuthenticationTestFactory;
import ru.netology.cloudstorage.webapp.model.AppAuthToken;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppAuthHeaderAuthenticationProviderTest {

    private final AuthenticationTestFactory authenticationTestFactory = new AuthenticationTestFactory();

    @Mock
    private AuthTokenManager tokenManager;

    public static Stream<Arguments> supports() {
        return Stream.of(
                Arguments.of(PreAuthenticatedAuthenticationToken.class, true),
                Arguments.of(TestNewPreAuthenticatedAuthenticationToken.class, false),
                Arguments.of(UsernamePasswordAuthenticationToken.class, false),
                Arguments.of(JaasAuthenticationToken.class, false),
                Arguments.of(AbstractAuthenticationToken.class, false)
        );
    }

    @Test
    void givenExistedToken_whenAuthenticate_thenAuthenticatedToken() {
        String tokenValue = "nDPW8dJEVEmE2TMTiiftzbLriz638NLFPntdF962a62fX0cV";
        String rawToken = "Bearer " + tokenValue;
        UserDetails user = authenticationTestFactory.createUser();
        AuthToken authToken = new AppAuthToken(user.getAuthorities(), user, tokenValue);
        Mockito.when(tokenManager.getToken(rawToken)).thenReturn(Optional.of(authToken));
        Authentication authentication = new PreAuthenticatedAuthenticationToken(rawToken, null);
        AuthenticationProvider sut = new AppAuthHeaderAuthenticationProvider(tokenManager);

        Authentication result = sut.authenticate(authentication);

        assertNotNull(result);
        assertInstanceOf(PreAuthenticatedAuthenticationToken.class, result);
        assertTrue(result.isAuthenticated());
        assertNull(result.getCredentials());
        assertIterableEquals(user.getAuthorities(), result.getAuthorities());
        assertEquals(user, result.getPrincipal());
    }

    @Test
    void givenMissingToken_whenAuthenticate_thenNotAuthenticatedToken() {
        String tokenValue = "nDPW8dJEVEmE2TMTiiftzbLriz638NLFPntdF962a62fX0cV";
        String rawToken = "Bearer " + tokenValue;
        Mockito.when(tokenManager.getToken(rawToken)).thenReturn(Optional.empty());
        Authentication authentication = new PreAuthenticatedAuthenticationToken(rawToken, null);
        AuthenticationProvider sut = new AppAuthHeaderAuthenticationProvider(tokenManager);

        Authentication result = sut.authenticate(authentication);

        assertNotNull(result);
        assertInstanceOf(PreAuthenticatedAuthenticationToken.class, result);
        assertFalse(result.isAuthenticated());
        assertEquals(authentication, result);
    }

    @ParameterizedTest
    @MethodSource
    void supports(Class<?> authenticationClass, boolean expectedResult) {
        AppAuthHeaderAuthenticationProvider sut = new AppAuthHeaderAuthenticationProvider(tokenManager);

        boolean result = sut.supports(authenticationClass);

        assertEquals(expectedResult, result);
    }

    /**
     * For test only
     */
    private static class TestNewPreAuthenticatedAuthenticationToken extends PreAuthenticatedAuthenticationToken {

        public TestNewPreAuthenticatedAuthenticationToken(Object aPrincipal, Object aCredentials) {
            super(aPrincipal, aCredentials);
        }

        public TestNewPreAuthenticatedAuthenticationToken(Object aPrincipal, Object aCredentials,
                Collection<? extends GrantedAuthority> anAuthorities) {
            super(aPrincipal, aCredentials, anAuthorities);
        }
    }
}
