package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

public interface CreateCloudFileInputDbRepository {
    boolean save(CloudFile cloudFile);
}
