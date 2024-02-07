package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileInputDbRepository;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

import java.util.Optional;

@RequiredArgsConstructor
@Component("appDeleteCloudFileInputDbRepository")
public class AppDeleteCloudFileInputDbRepository implements DeleteCloudFileInputDbRepository {

    private final AppCloudFileJpaRepository cloudFileJpaRepository;

    private final AppStorageFileJpaRepository storageFileJpaRepository;

    @Override
    public Optional<CloudFile> findByUserAndFileNameAndReadyStatus(CloudUser user, String fileName) {
        return cloudFileJpaRepository.findByUserIdAndFileNameAndStatusLatest_Status_Code(user.getId(),
                        fileName, CloudFileStatusCode.READY)
                .map(file -> CoreCloudFile.from(file).updatedAt(file.getUpdatedAt()).build());
    }

    @Override
    public boolean delete(StorageFile storageFile) {
        storageFileJpaRepository.deleteById(storageFile.getId());
        return true;
    }

    @Override
    public boolean update(CloudFile cloudFile) {
        AppCloudFile appCloudFile = new AppCloudFile(cloudFile);
        cloudFileJpaRepository.saveAndFlush(appCloudFile);
        return true;
    }
}
