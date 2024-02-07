package ru.netology.cloudstorage.storage.local.model;

import lombok.Builder;
import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class LocalStorageFile implements StorageFile {
    private final long size;
    private final String fileName;
    private final String mediaType;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
