package ru.netology.cloudstorage.webapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.webapp.input.AppLoginRequest;
import ru.netology.cloudstorage.webapp.input.AppLoginResponse;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

@RestController
public class AppAuthController {

    private final AppAuthTokenProperties tokenProperties;

    private final AuthTokenManager appAuthTokenManager;

    public AppAuthController(AppAuthTokenProperties tokenProperties, AuthTokenManager appAuthTokenManager) {
        this.tokenProperties = tokenProperties;
        this.appAuthTokenManager = appAuthTokenManager;
    }

    @PostMapping("/login")
    public AppLoginResponse login(@Validated @RequestBody AppLoginRequest request, HttpServletRequest servletRequest) {

        try {
            servletRequest.login(request.getLogin(), request.getPassword());
        } catch (ServletException ex) {
            throw new BadCredentialsException("Invalid username or password", ex);
        }

        Authentication auth = (Authentication) servletRequest.getUserPrincipal();
        String tokenValue = appAuthTokenManager.createToken(auth);

        return new AppLoginResponse(tokenValue);
    }


    @PostMapping("/logout")
    public void logout(HttpServletRequest servletRequest) throws ServletException {
        appAuthTokenManager.revokeToken(servletRequest.getHeader(tokenProperties.getHeaderName()));
        servletRequest.logout();
    }
}
