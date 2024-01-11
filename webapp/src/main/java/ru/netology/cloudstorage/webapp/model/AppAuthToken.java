package ru.netology.cloudstorage.webapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.security.core.GrantedAuthority;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;

import java.io.Serializable;
import java.util.Collection;

@RequiredArgsConstructor
@Getter
@RedisHash("app-auth-token")
public class AppAuthToken implements AuthToken, Serializable {

    private final Collection<? extends GrantedAuthority> authorities;

    private final Object principal;

    @Id
    private final String value;

    @TimeToLive
    @Value("${cloudstorage.auth.token.redis-ttl:300}")
    private long ttl;
}
