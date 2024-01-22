package ru.netology.cloudstorage.contracts.core.exception;

import ru.netology.cloudstorage.contracts.core.model.Traceable;

public abstract class CloudFileException extends CloudstorageException implements Traceable {
    public CloudFileException(CloudFileExceptionCode message) {
        super(message.code);
    }

    public CloudFileException(CloudFileExceptionCode message, Throwable cause) {
        super(message.code, cause);
    }
}
