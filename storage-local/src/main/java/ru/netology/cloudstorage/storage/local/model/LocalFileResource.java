package ru.netology.cloudstorage.storage.local.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class LocalFileResource implements FileResource {
    private final long size;

    private final String fileName;

    private final String mediaType;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final Path storagePath;

    public LocalFileResource(StorageFile storageFile, Path storagePath) {
        this.fileName = storageFile.getFileName();
        this.mediaType = storageFile.getMediaType();
        this.createdAt = storageFile.getCreatedAt();
        this.updatedAt = storageFile.getUpdatedAt();
        this.size = storageFile.getSize();
        this.storagePath = storagePath;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(storagePath);
    }
}
