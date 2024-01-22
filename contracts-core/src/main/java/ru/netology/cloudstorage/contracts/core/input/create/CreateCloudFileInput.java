package ru.netology.cloudstorage.contracts.core.input.create;

public interface CreateCloudFileInput {
    CreateCloudFileInputResponse create(CreateCloudFileInputRequest request);
}
