package ru.netology.cloudstorage.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class AppAuthToken implements AuthToken {
    private final Collection<? extends GrantedAuthority> authorities;
    private final Object principal;
    private final String value;
}
