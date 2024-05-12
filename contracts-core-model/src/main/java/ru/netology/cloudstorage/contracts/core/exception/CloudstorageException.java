package ru.netology.cloudstorage.contracts.core.exception;

public abstract class CloudstorageException extends RuntimeException {
    protected CloudstorageException(String message) {
        super(message);
    }

    protected CloudstorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
