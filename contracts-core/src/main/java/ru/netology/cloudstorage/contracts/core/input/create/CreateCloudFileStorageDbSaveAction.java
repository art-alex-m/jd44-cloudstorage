package ru.netology.cloudstorage.contracts.core.input.create;

public interface CreateCloudFileStorageDbSaveAction {
    boolean save(CreateCloudFileStorageDbSaveActionRequest request);
}
