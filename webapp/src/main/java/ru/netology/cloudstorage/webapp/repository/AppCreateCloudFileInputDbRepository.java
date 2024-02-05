package ru.netology.cloudstorage.webapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;
import ru.netology.cloudstorage.webapp.entity.AppCloudFile;

@RequiredArgsConstructor
@Component("appCreateCloudFileInputDbRepository")
public class AppCreateCloudFileInputDbRepository implements CreateCloudFileInputDbRepository {

    private final AppCloudFileCrudRepository jpaRepository;

    @Override
    public boolean save(CloudFile cloudFile) {
        AppCloudFile entity = new AppCloudFile(cloudFile);
        jpaRepository.save(entity);
        return true;
    }
}
