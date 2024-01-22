package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;

import java.util.UUID;

public interface CloudFileInputResponse {
    UUID getCloudFileId();
    CloudFileStatus getCloudFileStatus();
}
