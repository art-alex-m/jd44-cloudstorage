package ru.netology.cloudstorage.webapp.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.security.core.GrantedAuthority;
import ru.netology.cloudstorage.contracts.auth.model.AuthToken;

import java.io.Serializable;
import java.util.Collection;

@Getter
@RedisHash("app-auth-token")
public class AppAuthToken implements AuthToken, Serializable {

    public static final long DEFAULT_TTL = 300;

    @Id
    private String value;

    private Collection<? extends GrantedAuthority> authorities;

    private Object principal;

    private long ttl;

    public AppAuthToken() {
    }

    public AppAuthToken(Collection<? extends GrantedAuthority> authorities, Object principal, String value) {
        this(authorities, principal, value, DEFAULT_TTL);
    }

    public AppAuthToken(Collection<? extends GrantedAuthority> authorities, Object principal, String value, long ttl) {
        this.authorities = authorities;
        this.principal = principal;
        this.value = value;
        this.ttl = ttl;
    }

    @TimeToLive
    public long getTtl() {
        return ttl;
    }
}
