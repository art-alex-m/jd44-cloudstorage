package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppDeleteCloudFileActionDbRepositoryTest {

    @Autowired
    AppCloudFileJpaRepository jpaRepository;

    @Autowired
    AppStorageFileJpaRepository fileJpaRepository;

    AppDeleteCloudFileActionDbRepository sut;

    @BeforeEach
    void setUp() {
        sut = new AppDeleteCloudFileActionDbRepository(jpaRepository);
    }

    @Test
    void givenCloudFile_whenDelete_thenSuccess() {
        UUID cloudFileId = UUID.fromString("7b7584a5-7391-359e-bca6-7e4e79b058e1");
        fileJpaRepository.deleteById(UUID.fromString("cfa0bf2d-0b46-4ff5-86e3-5d1ec8f05772"));
        CloudFile cloudFile = jpaRepository.findById(cloudFileId)
                .map(file -> CoreCloudFile.from(file).storageFile(null).build()).get();

        boolean result = sut.delete(cloudFile);

        assertTrue(result);
        assertFalse(jpaRepository.existsById(cloudFileId));
    }
}
