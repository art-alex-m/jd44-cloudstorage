package ru.netology.cloudstorage.webapp.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;

import java.util.Optional;


@Component
public class AppAuthHeaderAuthenticationProvider implements AuthenticationProvider {

    private final AuthTokenManager authTokenManager;

    public AppAuthHeaderAuthenticationProvider(AuthTokenManager appAuthTokenManager) {
        this.authTokenManager = appAuthTokenManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String tokenValue = authentication.getName();
        Optional<AuthToken> token = authTokenManager.getToken(tokenValue);

        if (token.isEmpty()) {
            return authentication;
        }

        return new PreAuthenticatedAuthenticationToken(token.get().getPrincipal(), null, token.get().getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
