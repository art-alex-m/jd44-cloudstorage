package ru.netology.cloudstorage.contracts.event.model.create;

import ru.netology.cloudstorage.contracts.core.model.FileResource;

public interface CloudFileDbCreated extends CreateCloudFileEvent {
    FileResource getUserFile();
}
