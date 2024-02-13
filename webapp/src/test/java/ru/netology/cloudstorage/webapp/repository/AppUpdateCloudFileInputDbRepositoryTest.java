package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppUpdateCloudFileInputDbRepositoryTest {

    @Autowired
    AppCloudFileJpaRepository crudRepository;

    AppUpdateCloudFileInputDbRepository sut;

    public static Stream<Arguments> givenUserAndFileName_whenFindByUserAndFileName_thenExpected() {
        UUID testUuid = UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519");
        return Stream.of(
                Arguments.of(new TestCloudUser(), "4", false),
                Arguments.of(new TestCloudUser(testUuid), "not-existed.file", false),
                Arguments.of(new TestCloudUser(testUuid), "4", true),
                Arguments.of(new TestCloudUser(testUuid), "5", true)
        );
    }

    @BeforeEach
    void setUp() {
        sut = new AppUpdateCloudFileInputDbRepository(crudRepository);
    }

    @ParameterizedTest
    @MethodSource
    void givenUserAndFileName_whenFindByUserAndFileName_thenExpected(CloudUser user, String fileName,
            boolean expected) {
        Optional<CloudFile> result = sut.findByUserAndFileName(user, fileName);

        assertEquals(expected, result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("givenUserAndFileName_whenFindByUserAndFileName_thenExpected")
    void givenUserAndFileName_whenUniqueNewName_thenExpected(CloudUser user, String fileName, boolean expected) {
        boolean result = sut.uniqueNewName(user, fileName);

        assertNotEquals(expected, result);
    }

    @Test
    void givenUserAndFileNameAndNewFileName_whenUpdate_thenSuccess() {
        String newFileName = "new-file-name";
        String oldFileName = "1";
        CloudUser user = new TestCloudUser(UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519"));
        CloudFile cloudFile = crudRepository.findByUserIdAndFileName(user.getId(), oldFileName).orElse(null);
        Objects.requireNonNull(cloudFile);
        CloudFile newCloudFile = CoreCloudFile.from(cloudFile).fileName(newFileName).build();

        boolean result = sut.update(newCloudFile);

        assertTrue(result);
        CloudFile resultFile = crudRepository.findById(cloudFile.getId()).orElse(null);
        assertNotNull(resultFile);
        assertEquals(newFileName, resultFile.getFileName());
        assertEquals(cloudFile.getStorageFile(), resultFile.getStorageFile());
        assertEquals(cloudFile.getUser().getId(), resultFile.getUser().getId());
    }

    @Test
    void givenUserAndFileNameAndInvalidNewName_whenUpdate_thenDataIntegrityViolation() {
        String newFileName = "2";
        String oldFileName = "1";
        CloudUser user = new TestCloudUser(UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519"));
        CloudFile cloudFile = crudRepository.findByUserIdAndFileName(user.getId(), oldFileName).orElse(null);
        Objects.requireNonNull(cloudFile);
        CloudFile newCloudFile = CoreCloudFile.from(cloudFile).fileName(newFileName).build();

        Executable result = () -> sut.update(newCloudFile);

        assertThrows(DataIntegrityViolationException.class, result);
    }
}
