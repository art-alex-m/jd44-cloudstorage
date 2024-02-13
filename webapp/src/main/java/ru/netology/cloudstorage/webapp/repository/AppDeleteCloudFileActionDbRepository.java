package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.DeleteCloudFileActionDbRepository;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

@RequiredArgsConstructor
@Component("appDeleteCloudFileActionDbRepository")
public class AppDeleteCloudFileActionDbRepository implements DeleteCloudFileActionDbRepository {

    private final AppCloudFileJpaRepository jpaRepository;

    @Override
    public boolean delete(CloudFile cloudFile) {
        AppCloudFile entity = new AppCloudFile(cloudFile);
        jpaRepository.delete(entity);

        return true;
    }
}
