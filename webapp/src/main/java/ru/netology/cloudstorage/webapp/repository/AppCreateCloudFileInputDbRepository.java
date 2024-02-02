package ru.netology.cloudstorage.webapp.repository;

import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.db.repository.CreateCloudFileInputDbRepository;

@Component("appCreateCloudFileInputDbRepository")
public class AppCreateCloudFileInputDbRepository implements CreateCloudFileInputDbRepository {
    @Override
    public boolean save(CloudFile cloudFile) {
        return true;
    }
}
