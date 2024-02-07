package ru.netology.cloudstorage.core.event.update;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.event.model.update.UpdateCloudFileError;

@Getter
@SuperBuilder
public class CoreUpdateCloudFileError extends CoreUpdateCloudFileEvent implements UpdateCloudFileError {
    private final String errorMessage;
}
