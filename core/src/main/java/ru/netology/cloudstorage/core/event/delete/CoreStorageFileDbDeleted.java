package ru.netology.cloudstorage.core.event.delete;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteActionRequest;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDbDeleted;

@SuperBuilder
@Getter
public class CoreStorageFileDbDeleted extends CoreDeleteCloudFileEvent
        implements StorageFileDbDeleted, DeleteCloudFileStorageDeleteActionRequest {
    @NonNull
    private final StorageFile storageFile;
}
