package ru.netology.cloudstorage.contracts.core.boundary.list;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputRequest;

public interface ListCloudFileInputRequest extends CloudFileInputRequest {
    int getLimit();
}
