package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

public interface CreateCloudFileStorageDbSaveAction {
    boolean save(CreateCloudFileStorageDbSaveActionRequest request) throws CloudFileException;
}
