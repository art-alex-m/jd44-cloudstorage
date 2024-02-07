package ru.netology.cloudstorage.core.event.create;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadActionRequest;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileDbCreated;

@Getter
@SuperBuilder
public class CoreCloudFileDbCreated extends CoreCreateCloudFileEvent
        implements CloudFileDbCreated, CreateCloudFileStorageUploadActionRequest {
    private final FileResource userFile;
}
