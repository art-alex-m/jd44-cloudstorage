package ru.netology.cloudstorage.core.event.delete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileEvent;

import java.time.Instant;

@SuperBuilder
@Getter
@AllArgsConstructor
public abstract class CoreDeleteCloudFileEvent implements DeleteCloudFileEvent {
    private final CloudFile cloudFile;

    private final TraceId traceId;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
