package ru.netology.cloudstorage.contracts.event.model.create;

import ru.netology.cloudstorage.contracts.storage.model.FileResource;

public interface CloudFileLoaded extends CreateCloudFileEvent {
    FileResource getUserFile();
}
