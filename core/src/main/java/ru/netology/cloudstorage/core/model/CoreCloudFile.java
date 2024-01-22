package ru.netology.cloudstorage.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class CoreCloudFile implements CloudFile {
    private final UUID id;
    private final String fileName;
    private final StorageFile storageFile;
    private final CloudUser user;
    private final CloudFileStatus status;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
