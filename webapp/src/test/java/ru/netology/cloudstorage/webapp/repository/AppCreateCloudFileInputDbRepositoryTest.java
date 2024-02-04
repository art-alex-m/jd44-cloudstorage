package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileIdFactory;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileIdFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestDockerized
class AppCreateCloudFileInputDbRepositoryTest {

    AppCreateCloudFileInputDbRepository sut;

    static TraceIdFactory traceIdFactory = new CoreTraceIdFactory();

    static CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    static CloudFileIdFactory idFactory = new CoreCloudFileIdFactory();

    @Autowired
    AppCloudFileJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        sut = new AppCreateCloudFileInputDbRepository(jpaRepository);
    }

    @Test
    void givenCloudFileLoading_whenSave_thenSuccess() {
        TraceId traceId = traceIdFactory.create();
        String fileName = "test-file-name.jpg";
        CloudUser cloudUser = new TestCloudUser();
        CloudFile cloudFile = CoreCloudFile.builder()
                .fileName(fileName)
                .id(idFactory.create(cloudUser.getId(), fileName))
                .user(cloudUser)
                .status(statusFactory.create(CloudFileStatusCode.LOADING, traceId))
                .build();

        boolean result = sut.save(cloudFile);

        assertTrue(result);
        Optional<AppCloudFile> loadedFile = jpaRepository.findById(cloudFile.getId());
        assertTrue(loadedFile.isPresent());
    }
}