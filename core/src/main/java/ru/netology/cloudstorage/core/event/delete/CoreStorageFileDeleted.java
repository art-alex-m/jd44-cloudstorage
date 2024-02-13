package ru.netology.cloudstorage.core.event.delete;

import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileActionRequest;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDeleted;

@SuperBuilder
public class CoreStorageFileDeleted extends CoreDeleteCloudFileEvent
        implements StorageFileDeleted, DeleteCloudFileActionRequest {
}
