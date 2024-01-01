package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.entity.AppUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTestDockerized
@Sql("/db/fixture/users/01-insert-test-user.sql")
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository sut;

    @ParameterizedTest
    @CsvSource({"test@example.com, true", "not-found@example.com, false"})
    void findByUsername(String username, boolean found) {
        Optional<AppUser> result = sut.findByUsername(username);

        assertEquals(found, result.isPresent());
    }
}