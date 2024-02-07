package ru.netology.cloudstorage.contracts.core.boundary.delete;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileActionRequest;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

public interface DeleteCloudFileStorageDeleteActionRequest extends CloudFileActionRequest {
    StorageFile getStorageFile();
}
