package ru.netology.cloudstorage.core.event.create;

import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyActionRequest;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileDbStored;

@SuperBuilder
public class CoreStorageFileDbStored extends CoreCreateCloudFileEvent
        implements StorageFileDbStored, CreateCloudFileReadyActionRequest {
}
