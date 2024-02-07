package ru.netology.cloudstorage.contracts.event.exception;

import ru.netology.cloudstorage.contracts.core.exception.CloudstorageException;

public class CloudstorageEventPublisherException extends CloudstorageException {
    public CloudstorageEventPublisherException(String message) {
        super(message);
    }

    public CloudstorageEventPublisherException(String message, Throwable cause) {
        super(message, cause);
    }
}
