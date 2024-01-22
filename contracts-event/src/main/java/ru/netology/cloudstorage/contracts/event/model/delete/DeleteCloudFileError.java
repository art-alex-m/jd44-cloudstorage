package ru.netology.cloudstorage.contracts.event.model.delete;

import ru.netology.cloudstorage.contracts.event.model.CloudFileError;

public interface DeleteCloudFileError extends DeleteCloudFileEvent, CloudFileError {
}
