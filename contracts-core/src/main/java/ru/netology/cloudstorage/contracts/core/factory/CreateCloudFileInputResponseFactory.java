package ru.netology.cloudstorage.contracts.core.factory;

import ru.netology.cloudstorage.contracts.core.input.CreateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;

public interface CreateCloudFileInputResponseFactory {
    CreateCloudFileInputResponse create(CloudFile cloudFile);
}
