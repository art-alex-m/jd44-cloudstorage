package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

public interface CreateCloudFileInput {
    CreateCloudFileInputResponse create(CreateCloudFileInputRequest request) throws CloudFileException;
}
