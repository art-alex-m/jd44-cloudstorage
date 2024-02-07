package ru.netology.cloudstorage.webapp.factory;

import lombok.Getter;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.netology.cloudstorage.contracts.auth.model.PermissionFiles;
import ru.netology.cloudstorage.webapp.entity.AppAuthority;
import ru.netology.cloudstorage.webapp.entity.AppUser;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@TestComponent
@Getter
public class AuthenticationTestFactory {

    private final UUID testUserId = UUID.fromString("00a35c6a-ffd1-4a17-8d53-2001b8a0e700");
    private final String testUsername = "test@example.com";
    private final String testUserPassword = "123456";
    private final String testTokenValue = "some-test-token-value";

    public Authentication createAuthentication() {
        AppUser user = createUser();

        return new PreAuthenticatedAuthenticationToken(user, null, user.getAuthorities());
    }

    public AppUser createUser() {
        AppUser user = createUser(true);
        user.setAuthorities(createAuthorities());

        return user;
    }

    public AppUser createUser(boolean withoutDefaultAuthorities) {
        AppUser user = new AppUser();
        user.setId(testUserId);
        user.setUsername(testUsername);
        user.setEnabled(true);
        user.setPassword("[PROTECTED]");

        return user;
    }

    public Set<AppAuthority> createAuthorities() {
        return PermissionFiles.USER_ALL.stream()
                .map(a -> new AppAuthority(new AppAuthority.PK(testUsername, a)))
                .collect(Collectors.toSet());
    }
}
