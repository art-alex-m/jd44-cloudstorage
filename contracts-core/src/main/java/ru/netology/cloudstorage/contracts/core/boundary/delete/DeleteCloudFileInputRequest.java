package ru.netology.cloudstorage.contracts.core.boundary.delete;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputRequest;

public interface DeleteCloudFileInputRequest extends CloudFileInputRequest {
    String getFileName();
}
