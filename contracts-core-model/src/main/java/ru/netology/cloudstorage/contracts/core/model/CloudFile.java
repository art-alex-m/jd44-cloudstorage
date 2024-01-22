package ru.netology.cloudstorage.contracts.core.model;

import ru.netology.cloudstorage.contracts.base.model.Timestamps;
import ru.netology.cloudstorage.contracts.storage.model.StorageFile;

import java.util.UUID;

public interface CloudFile extends Timestamps {
    UUID getId();

    String getFileName();

    StorageFile getStorageFile();

    CloudUser getUser();

    CloudFileStatus getStatus();
}
