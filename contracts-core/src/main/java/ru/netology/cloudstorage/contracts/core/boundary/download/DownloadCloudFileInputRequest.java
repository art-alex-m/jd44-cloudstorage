package ru.netology.cloudstorage.contracts.core.boundary.download;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputRequest;

public interface DownloadCloudFileInputRequest extends CloudFileInputRequest {
    String getFileName();
}
