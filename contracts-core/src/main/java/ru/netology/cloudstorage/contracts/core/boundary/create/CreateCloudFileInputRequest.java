package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.boundary.CloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.model.FileResource;


public interface CreateCloudFileInputRequest extends CloudFileInputRequest {
    FileResource getUserFile();
}
