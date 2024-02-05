package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.*;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.storage.local.model.LocalStorageFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTestDockerized
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppCreateCloudFileInputDbRepositoryTest {

    static TraceIdFactory traceIdFactory = new CoreTraceIdFactory();

    static CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    static CloudUser cloudUser = new TestCloudUser();

    static TraceId traceId = traceIdFactory.create();

    static String fileName = "test-file-name.jpg";

    static String statusMessage = "some test message";

    static CoreCloudFile baseCloudFile = CoreCloudFile.builder()
            .fileName(fileName)
            .id(UUID.randomUUID())
            .user(cloudUser)
            .build();

    @Autowired
    AppCloudFileJpaRepository jpaRepository;

    AppCreateCloudFileInputDbRepository sut;

    @BeforeEach
    void setUp() {
        sut = new AppCreateCloudFileInputDbRepository(jpaRepository);
    }

    @Test
    @Order(100)
    void givenCloudFileLoading_whenSave_thenSuccess() {
        CloudFile cloudFile = baseCloudFile.toBuilder()
                .status(statusFactory.create(CloudFileStatusCode.LOADING, traceId, statusMessage))
                .build();

        boolean result = sut.save(cloudFile);

        assertTrue(result);
        AppCloudFile loadedFile = jpaRepository.findById(cloudFile.getId()).orElse(null);
        assertNotNull(loadedFile);
        assertEquals(cloudFile.getCreatedAt(), loadedFile.getCreatedAt());
        assertEquals(cloudFile.getUpdatedAt(), loadedFile.getUpdatedAt());
        assertEquals(cloudFile.getFileName(), loadedFile.getFileName());
        assertEquals(cloudFile.getUser().getId(), loadedFile.getUser().getId());
        assertEquals(cloudFile.getStatus().getId(), loadedFile.getStatus().getId());
        assertEquals(statusMessage, loadedFile.getStatus().getMessage());
        assertEquals(cloudFile.getStatus().getCreatedAt(), loadedFile.getStatus().getCreatedAt());
        assertEquals(cloudFile.getStatus().getUpdatedAt(), loadedFile.getStatus().getUpdatedAt());
        assertEquals(CloudFileStatusCode.LOADING, loadedFile.getStatus().getCode());
        assertEquals(traceId.getUuid(), loadedFile.getStatus().getTraceId().getUuid());
        assertEquals(traceId.getId(), loadedFile.getStatus().getTraceId().getId());
    }

    @Test
    @Order(120)
    void givenCloudFileUploaded_whenSave_thenSuccess() {
        jpaRepository.findById(baseCloudFile.getId());
        StorageFile storageFile = LocalStorageFile.builder()
                .size(1L)
                .fileName(traceId.getUuid().toString())
                .mediaType("some-media-type")
                .build();
        CoreCloudFile cloudFile = baseCloudFile.toBuilder()
                .storageFile(storageFile)
                .status(statusFactory.create(CloudFileStatusCode.UPLOADED, traceId))
                .build();

        boolean result = sut.save(cloudFile);

        assertTrue(result);
        AppCloudFile loadedFile = jpaRepository.findById(baseCloudFile.getId()).orElse(null);
        assertNotNull(loadedFile);
        assertNotNull(loadedFile.getStorageFile());
        assertEquals(storageFile.getId(), loadedFile.getStorageFile().getId());
        assertEquals(storageFile.getFileName(), loadedFile.getStorageFile().getFileName());
        assertEquals(storageFile.getCreatedAt(), loadedFile.getStorageFile().getCreatedAt());
        assertEquals(storageFile.getUpdatedAt(), loadedFile.getStorageFile().getUpdatedAt());
        assertEquals(storageFile.getSize(), loadedFile.getStorageFile().getSize());
        assertEquals(storageFile.getMediaType(), loadedFile.getStorageFile().getMediaType());
        assertNotNull(loadedFile.getStatus());
        assertNull(loadedFile.getStatus().getMessage());
        assertEquals(cloudFile.getStatus().getCreatedAt(), loadedFile.getStatus().getCreatedAt());
        assertEquals(cloudFile.getStatus().getUpdatedAt(), loadedFile.getStatus().getUpdatedAt());
        assertEquals(CloudFileStatusCode.UPLOADED, loadedFile.getStatus().getCode());
        assertEquals(traceId.getUuid(), loadedFile.getStatus().getTraceId().getUuid());
        assertEquals(traceId.getId(), loadedFile.getStatus().getTraceId().getId());
    }
}
