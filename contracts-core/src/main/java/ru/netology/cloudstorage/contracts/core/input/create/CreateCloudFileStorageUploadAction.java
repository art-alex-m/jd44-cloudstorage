package ru.netology.cloudstorage.contracts.core.input.create;

public interface CreateCloudFileStorageUploadAction {
    boolean upload(CreateCloudFileStorageUploadActionRequest request);
}
