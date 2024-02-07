package ru.netology.cloudstorage.contracts.event.model;

public interface CloudFileError extends CloudFileEvent {
    String getErrorMessage();
}
