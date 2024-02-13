package ru.netology.cloudstorage.core.event.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.event.model.update.UpdateCloudFileEvent;

import java.time.Instant;

@AllArgsConstructor
@Getter
@SuperBuilder
public abstract class CoreUpdateCloudFileEvent implements UpdateCloudFileEvent {
    private final CloudFile cloudFile;

    private final TraceId traceId;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
