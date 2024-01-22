package ru.netology.cloudstorage.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CoreCloudFileStatus implements CloudFileStatus {
    private final UUID id;
    private final CloudFileStatusCode code;
    private final String message;
    private final TraceId traceId;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
