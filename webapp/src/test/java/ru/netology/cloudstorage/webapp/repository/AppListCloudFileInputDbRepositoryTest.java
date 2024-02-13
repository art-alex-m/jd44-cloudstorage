package ru.netology.cloudstorage.webapp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.webapp.config.DataJpaTestDockerized;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTestDockerized
@Sql("/db/fixture/cloud-files/01-insert-test-cloudfiles.sql")
class AppListCloudFileInputDbRepositoryTest {

    @Autowired
    AppCloudFileJpaRepository crudRepository;

    @Test
    void givenCloudUserAndLimit_whenFindByUserAndReadyStatus_thenSuccess() {
        CloudUser user = new TestCloudUser(UUID.fromString("09b35c6a-ffd1-4a17-8d53-2001b8a0e519"));
        int testLimit = 4;
        AppListCloudFileInputDbRepository sut = new AppListCloudFileInputDbRepository(crudRepository);

        List<CloudFile> result = sut.findByUserAndReadyStatus(user, testLimit);

        assertNotNull(result);
        assertEquals(testLimit, result.size());
        List<String> resultFileNames = result.stream().map(CloudFile::getFileName).toList();
        assertIterableEquals(List.of("5", "3", "2", "1"), resultFileNames);
    }

    @Test
    void givenUnknownUserAndLimit_whenFindByUserAndReadyStatus_thenSuccessEmptyList() {
        CloudUser user = new TestCloudUser();
        int testLimit = 3;
        AppListCloudFileInputDbRepository sut = new AppListCloudFileInputDbRepository(crudRepository);

        List<CloudFile> result = sut.findByUserAndReadyStatus(user, testLimit);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
