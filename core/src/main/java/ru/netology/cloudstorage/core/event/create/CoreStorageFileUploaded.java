package ru.netology.cloudstorage.core.event.create;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveActionRequest;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileUploaded;

@Getter
@SuperBuilder
public class CoreStorageFileUploaded extends CoreCreateCloudFileEvent
        implements StorageFileUploaded, CreateCloudFileStorageDbSaveActionRequest {
}
