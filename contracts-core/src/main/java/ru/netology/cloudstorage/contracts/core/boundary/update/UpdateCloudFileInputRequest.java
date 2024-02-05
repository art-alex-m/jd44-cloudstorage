package ru.netology.cloudstorage.contracts.core.boundary.update;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputRequest;

public interface UpdateCloudFileInputRequest extends CloudFileInputRequest {
    String getFileName();

    String getNewFileName();
}
