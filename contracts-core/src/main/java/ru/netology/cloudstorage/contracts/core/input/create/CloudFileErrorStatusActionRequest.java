package ru.netology.cloudstorage.contracts.core.input.create;

import ru.netology.cloudstorage.contracts.core.input.CloudFileActionRequest;

public interface CloudFileErrorStatusActionRequest extends CloudFileActionRequest {
    String getErrorMessage();
}
