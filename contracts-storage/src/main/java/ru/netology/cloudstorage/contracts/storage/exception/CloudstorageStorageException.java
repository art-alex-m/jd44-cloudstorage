package ru.netology.cloudstorage.contracts.storage.exception;

import ru.netology.cloudstorage.contracts.core.exception.CloudstorageException;

public class CloudstorageStorageException extends CloudstorageException {
    public CloudstorageStorageException(String message) {
        super(message);
    }

    public CloudstorageStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudstorageStorageException(CloudstorageStorageExceptionCode code, Throwable cause) {
        this(code.code, cause);
    }
}
