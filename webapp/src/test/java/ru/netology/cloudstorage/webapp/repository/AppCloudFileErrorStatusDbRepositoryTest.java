package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.core.factory.CoreCloudFileStatusFactory;
import ru.netology.cloudstorage.core.factory.CoreTraceIdFactory;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppCloudFileErrorStatusDbRepositoryTest {
    static CloudFileStatusFactory statusFactory = new CoreCloudFileStatusFactory();

    static TraceIdFactory traceIdFactory = new CoreTraceIdFactory();

    @Autowired
    AppCloudFileJpaRepository jpaRepository;

    AppCloudFileErrorStatusDbRepository sut;

    @BeforeEach
    void setUp() {
        sut = new AppCloudFileErrorStatusDbRepository(jpaRepository);
    }

    @Test
    void givenCloudFile_whenSave_thenSuccess() {
        UUID cloudFileId = UUID.fromString("730f651e-14e8-34f6-90f4-3ef7b49030ca");
        String errorMessage = "Error message";
        TraceId traceId = traceIdFactory.create();
        CloudFileStatus errorStatus = statusFactory.create(CloudFileStatusCode.ERROR, traceId, errorMessage);
        CloudFile cloudFile = jpaRepository.findById(cloudFileId).get();
        UUID startStatusId = cloudFile.getStatus().getId();
        CloudFile errorFile = CoreCloudFile.from(cloudFile).updatedAt(cloudFile.getUpdatedAt())
                .status(errorStatus).build();

        boolean result = sut.save(errorFile);

        assertTrue(result);
        assertNotEquals(startStatusId, cloudFile.getStatus().getId());
        assertEquals(errorStatus.getId(), cloudFile.getStatus().getId());
    }
}
