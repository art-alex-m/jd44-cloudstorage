package ru.netology.cloudstorage.contracts.core.factory;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

public interface CloudFileExceptionFactory {
    CloudFileException create(CloudFileExceptionCode code, TraceId traceId);

    CloudFileException create(CloudFileExceptionCode code, TraceId traceId, Throwable cause);
}
