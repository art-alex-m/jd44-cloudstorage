package ru.netology.cloudstorage.contracts.core.boundary;

import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;

import java.util.UUID;

/**
 * Базовая структура ответов сценариев
 */
public interface CloudFileInputResponse {
    UUID getCloudFileId();
    CloudFileStatus getCloudFileStatus();
}
