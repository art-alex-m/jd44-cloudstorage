package ru.netology.cloudstorage.contracts.core.boundary;

public interface CloudFileErrorStatusActionRequest extends CloudFileActionRequest {
    String getErrorMessage();
}
