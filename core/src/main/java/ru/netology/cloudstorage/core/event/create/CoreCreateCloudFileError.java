package ru.netology.cloudstorage.core.event.create;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;

@Getter
@SuperBuilder
public class CoreCreateCloudFileError extends CoreCreateCloudFileEvent
        implements CreateCloudFileError, CloudFileErrorStatusActionRequest {
    private final String errorMessage;
}
