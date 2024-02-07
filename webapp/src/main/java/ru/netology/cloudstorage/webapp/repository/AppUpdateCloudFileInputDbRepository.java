package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.db.repository.UpdateCloudFileInputDbRepository;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

import java.util.Optional;

@RequiredArgsConstructor
@Component("appUpdateCloudFileInputDbRepository")
public class AppUpdateCloudFileInputDbRepository implements UpdateCloudFileInputDbRepository {

    private final AppCloudFileJpaRepository jpaRepository;

    @Override
    public Optional<CloudFile> findByUserAndFileName(CloudUser user, String fileName) {
        return jpaRepository.findByUserIdAndFileName(user.getId(), fileName);
    }

    @Override
    public boolean uniqueNewName(CloudUser user, String fileName) {
        return !jpaRepository.existsByUserIdAndFileName(user.getId(), fileName);
    }

    @Override
    public boolean update(CloudFile cloudFile) {
        AppCloudFile entity = new AppCloudFile(cloudFile);
        jpaRepository.saveAndFlush(entity);
        return true;
    }
}
