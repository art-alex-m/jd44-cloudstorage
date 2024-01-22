package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

public interface CloudFileActionRequest {
    CloudFile getCloudFile();
}
