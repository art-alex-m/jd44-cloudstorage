package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

public interface CreateCloudFileStorageUploadAction {
    boolean upload(CreateCloudFileStorageUploadActionRequest request) throws CloudFileException;
}
