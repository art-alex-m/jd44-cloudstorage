package ru.netology.cloudstorage.contracts.auth.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthToken {

    String getValue();

    Collection<? extends GrantedAuthority> getAuthorities();

    Object getPrincipal();
}
