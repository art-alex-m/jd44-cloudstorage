package ru.netology.cloudstorage.core.factory;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileExceptionFactory;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

public class CoreCloudFileExceptionFactory implements CloudFileExceptionFactory {
    @Override
    public CloudFileException create(CloudFileExceptionCode code, TraceId traceId) {
        return new CloudFileException(code, traceId);
    }

    @Override
    public CloudFileException create(CloudFileExceptionCode code, TraceId traceId, Throwable cause) {
        return new CloudFileException(code, traceId, cause);
    }
}
