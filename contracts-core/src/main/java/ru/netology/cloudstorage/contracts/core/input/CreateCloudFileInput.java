package ru.netology.cloudstorage.contracts.core.input;

public interface CreateCloudFileInput {
    CreateCloudFileInputResponse create(CreateCloudFileInputRequest request);
}
