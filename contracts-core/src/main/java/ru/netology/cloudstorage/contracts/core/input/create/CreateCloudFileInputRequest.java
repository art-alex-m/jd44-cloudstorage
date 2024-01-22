package ru.netology.cloudstorage.contracts.core.input.create;

import ru.netology.cloudstorage.contracts.core.input.CloudFileInputRequest;
import ru.netology.cloudstorage.contracts.storage.model.FileResource;


public interface CreateCloudFileInputRequest extends CloudFileInputRequest {
    FileResource getUserFile();
}
