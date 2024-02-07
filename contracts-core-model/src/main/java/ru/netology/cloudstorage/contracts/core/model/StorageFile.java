package ru.netology.cloudstorage.contracts.core.model;

import java.util.UUID;

public interface StorageFile extends FileInfo {
    UUID getId();
}
