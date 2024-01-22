package ru.netology.cloudstorage.contracts.storage.model;

import java.util.UUID;

public interface StorageFile extends FileInfo {
    UUID getId();
}
