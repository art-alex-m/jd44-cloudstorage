package ru.netology.cloudstorage.core.event.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileEvent;

import java.time.Instant;


@AllArgsConstructor
@Getter
@SuperBuilder
public abstract class CoreCreateCloudFileEvent implements CreateCloudFileEvent {
    private final CloudFile cloudFile;

    private final TraceId traceId;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
