package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppDownloadCloudFileInputDbRepositoryTest {

    @Autowired
    AppCloudFileJpaRepository jpaRepository;

    AppDownloadCloudFileInputDbRepository sut;

    public static Stream<Arguments> givenUserFileName_whenFindByUserAndFileNameAndReadyStatus_thenExpected() {
        UUID testUuid = UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519");
        return Stream.of(
                Arguments.of(new TestCloudUser(), "4", false),
                Arguments.of(new TestCloudUser(testUuid), "not-existed.file", false),
                Arguments.of(new TestCloudUser(testUuid), "4", false),
                Arguments.of(new TestCloudUser(testUuid), "5", true)
        );
    }

    @BeforeEach
    void setUp() {
        sut = new AppDownloadCloudFileInputDbRepository(jpaRepository);
    }

    @ParameterizedTest
    @MethodSource
    void givenUserFileName_whenFindByUserAndFileNameAndReadyStatus_thenExpected(CloudUser cloudUser,
            String fileName, boolean expected) {

        Optional<CloudFile> result = sut.findByUserAndFileNameAndReadyStatus(cloudUser, fileName);

        assertEquals(expected, result.isPresent());
    }
}