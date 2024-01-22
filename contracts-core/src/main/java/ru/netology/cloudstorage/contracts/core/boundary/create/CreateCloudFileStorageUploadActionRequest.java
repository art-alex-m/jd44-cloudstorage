package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileActionRequest;
import ru.netology.cloudstorage.contracts.core.model.FileResource;

public interface CreateCloudFileStorageUploadActionRequest extends CloudFileActionRequest {
    FileResource getUserFile();
}
