package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.DownloadCloudFileInputDbRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component("appDownloadCloudFileInputDbRepository")
public class AppDownloadCloudFileInputDbRepository implements DownloadCloudFileInputDbRepository {

    private final AppCloudFileJpaRepository jpaRepository;

    @Override
    public Optional<CloudFile> findByUserAndFileNameAndReadyStatus(CloudUser user, String fileName) {
        return jpaRepository
                .findByUserIdAndFileNameAndStatusLatest_Status_Code(user.getId(), fileName, CloudFileStatusCode.READY);
    }
}
