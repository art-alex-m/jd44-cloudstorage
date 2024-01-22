package ru.netology.cloudstorage.core.factory;

import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.core.model.CoreCloudFileStatus;

import java.time.Instant;
import java.util.UUID;

public class CoreCloudFileStatusFactory implements CloudFileStatusFactory {
    @Override
    public CloudFileStatus create(CloudFileStatusCode code, TraceId traceId, String message) {
        return CoreCloudFileStatus.builder()
                .id(UUID.randomUUID())
                .code(code)
                .traceId(traceId)
                .message(message)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}
