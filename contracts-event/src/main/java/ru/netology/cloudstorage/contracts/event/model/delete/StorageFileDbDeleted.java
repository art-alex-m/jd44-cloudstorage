package ru.netology.cloudstorage.contracts.event.model.delete;

import ru.netology.cloudstorage.contracts.core.model.StorageFile;

public interface StorageFileDbDeleted extends DeleteCloudFileEvent {
    StorageFile getStorageFile();
}
