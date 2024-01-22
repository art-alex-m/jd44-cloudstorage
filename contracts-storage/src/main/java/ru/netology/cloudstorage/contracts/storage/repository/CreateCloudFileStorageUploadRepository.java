package ru.netology.cloudstorage.contracts.storage.repository;

import ru.netology.cloudstorage.contracts.storage.model.FileResource;
import ru.netology.cloudstorage.contracts.storage.model.StorageFile;

public interface CreateCloudFileStorageUploadRepository {
    StorageFile save(FileResource resource);
}
