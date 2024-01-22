package ru.netology.cloudstorage.contracts.core.exception;

public abstract class CloudstorageException extends RuntimeException {
    public CloudstorageException(String message) {
        super(message);
    }

    public CloudstorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
