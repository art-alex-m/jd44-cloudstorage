package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppDeleteCloudFileInputDbRepositoryTest {

    @Autowired
    AppCloudFileJpaRepository cloudFileJpaRepository;

    @Autowired
    AppStorageFileJpaRepository storageFileJpaRepository;

    AppDeleteCloudFileInputDbRepository sut;

    @BeforeEach
    void setUp() {
        sut = new AppDeleteCloudFileInputDbRepository(cloudFileJpaRepository, storageFileJpaRepository);
    }

    @ParameterizedTest
    @MethodSource("ru.netology.cloudstorage.webapp.repository.AppDownloadCloudFileInputDbRepositoryTest#givenUserFileName_whenFindByUserAndFileNameAndReadyStatus_thenExpected")
    void givenUserFileName_whenFindByUserAndFileNameAndReadyStatus_thenExpected(CloudUser cloudUser,
            String fileName, boolean expected) {

        Optional<CloudFile> result = sut.findByUserAndFileNameAndReadyStatus(cloudUser, fileName);

        assertEquals(expected, result.isPresent());
    }

    @Test
    void givenUserFileName_whenFindByUserAndFileNameAndReadyStatus_thenExpectedDifferentObjects() {
        String fileName = "3";
        CloudUser cloudUser = new TestCloudUser(UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519"));

        Optional<CloudFile> result = sut.findByUserAndFileNameAndReadyStatus(cloudUser, fileName);

        assertTrue(result.isPresent());
        Optional<CloudFile> resultRepo = cloudFileJpaRepository
                .findByUserIdAndFileNameAndStatusLatest_Status_Code(cloudUser.getId(), fileName,
                        CloudFileStatusCode.READY);
        assertTrue(resultRepo.isPresent());
        assertEquals(resultRepo.get().getId(), result.get().getId());
        assertNotEquals(resultRepo.get(), result.get());
    }

    @Test
    void givenStorageFile_whenDelete_thenSuccess() {
        UUID cloudFileId = UUID.fromString("7b7584a5-7391-359e-bca6-7e4e79b058e1");
        CloudFile cloudFile = cloudFileJpaRepository.findById(cloudFileId).get();
        StorageFile storageFile = cloudFile.getStorageFile();

        boolean result = sut.delete(storageFile);

        assertTrue(result);
        assertNotNull(cloudFile.getStorageFile());
        assertTrue(storageFileJpaRepository.findById(storageFile.getId()).isEmpty());
    }

    @Test
    void givenCloudFile_whenUpdate_thenSuccess() {
        TraceId traceId = new CoreTraceIdFactory().create();
        CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();
        UUID cloudFileId = UUID.fromString("89349b51-fba7-34b6-86ee-836737a460e8");
        String fileName = "2";
        CloudUser cloudUser = new TestCloudUser(UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519"));
        CloudFile cloudFile = sut.findByUserAndFileNameAndReadyStatus(cloudUser, fileName).orElse(null);
        Objects.requireNonNull(cloudFile);
        UUID storageFileId = cloudFile.getStorageFile().getId();
        sut.delete(cloudFile.getStorageFile());
        CloudFile updatedCloudFile = CoreCloudFile.from(cloudFile)
                .storageFile(null)
                .status(statusFactory.create(CloudFileStatusCode.DELETED, traceId))
                .build();

        boolean result = sut.update(updatedCloudFile);

        assertTrue(result);
        assertTrue(storageFileJpaRepository.findById(storageFileId).isEmpty());
        assertNotNull(cloudFile.getStorageFile());
        CloudFile cloudFileUpdated = cloudFileJpaRepository.findById(cloudFileId).orElse(null);
        assertNotNull(cloudFileUpdated);
        assertNull(cloudFileUpdated.getStorageFile());
        assertEquals(CloudFileStatusCode.DELETED, cloudFileUpdated.getStatus().getCode());
        assertTrue(cloudFileUpdated.getStatus().getUpdatedAt().isAfter(cloudFile.getStatus().getUpdatedAt()));
    }
}
