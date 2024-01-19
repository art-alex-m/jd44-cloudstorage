package ru.netology.cloudstorage.contracts.core.model;

import java.util.UUID;

public interface CloudFile extends Timestamps {
    UUID getId();

    String getFileName();

    StorageFile getStorageFile();

    CloudUser getUser();

    CloudFileStatus getStatus();
}
