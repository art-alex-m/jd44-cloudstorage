package ru.netology.cloudstorage.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "cloudstorage.auth.token")
public class AppAuthTokenProperties {
    private final int length;
    private final String headerName;
    private final long redisTtl;
    private final int redisToken;
}
