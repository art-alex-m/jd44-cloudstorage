package ru.netology.cloudstorage.core.event.delete;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;

@SuperBuilder
@Getter
public class CoreDeleteCloudFileError extends CoreDeleteCloudFileEvent
        implements DeleteCloudFileError, CloudFileErrorStatusActionRequest {
    public final String errorMessage;
}
