package ru.netology.cloudstorage.contracts.core.input.create;

import ru.netology.cloudstorage.contracts.core.input.CloudFileActionRequest;
import ru.netology.cloudstorage.contracts.storage.model.FileResource;

public interface CreateCloudFileStorageUploadActionRequest extends CloudFileActionRequest {
    FileResource getUserFile();
}
