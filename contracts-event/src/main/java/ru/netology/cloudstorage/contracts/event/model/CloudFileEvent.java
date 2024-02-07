package ru.netology.cloudstorage.contracts.event.model;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

public interface CloudFileEvent extends CloudstorageEvent {
    CloudFile getCloudFile();
}
