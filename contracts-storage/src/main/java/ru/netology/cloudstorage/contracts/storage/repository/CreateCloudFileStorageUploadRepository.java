package ru.netology.cloudstorage.contracts.storage.repository;


import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

public interface CreateCloudFileStorageUploadRepository {
    StorageFile save(FileResource resource);
}
