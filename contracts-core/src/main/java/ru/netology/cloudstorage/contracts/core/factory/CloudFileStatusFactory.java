package ru.netology.cloudstorage.contracts.core.factory;

import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

public interface CloudFileStatusFactory {
    CloudFileStatus create(CloudFileStatusCode code, TraceId traceId, String message);

    default CloudFileStatus create(CloudFileStatusCode code, TraceId traceId) {
        return create(code, traceId, null);
    }
}
