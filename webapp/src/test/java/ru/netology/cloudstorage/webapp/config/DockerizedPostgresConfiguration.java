package ru.netology.cloudstorage.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("test")
@TestConfiguration(proxyBeanMethods = false)
public class DockerizedPostgresConfiguration {

    @Value("${cloudstorage.tests.postgres.docker.tag}")
    protected String postgresDockerTag;

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse(postgresDockerTag));
    }
}
