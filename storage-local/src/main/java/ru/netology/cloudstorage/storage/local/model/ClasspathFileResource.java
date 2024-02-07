package ru.netology.cloudstorage.storage.local.model;

import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.storage.exception.CloudstorageStorageException;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/**
 * Представление FileResource для локального файла из ресурсов пакета
 */
@Getter
public class ClasspathFileResource implements FileResource {
    private final long size;
    private final String fileName;
    private final String mediaType;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final Path classPathFile;

    private final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public ClasspathFileResource(Path classPathFile) {
        this.classPathFile = classPathFile;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.fileName = classPathFile.getFileName().toString();
        try {
            URI classPathUrl = loader.getResource(classPathFile.toString()).toURI();
            Path filePath = Path.of(classPathUrl);
            this.size = Files.size(filePath);
            this.mediaType = Files.probeContentType(filePath);
        } catch (Exception ex) {
            throw new CloudstorageStorageException("Classpath file error reading " + fileName, ex);
        }
    }

    @Override
    public InputStream getInputStream() {
        return loader.getResourceAsStream(classPathFile.toString());
    }
}
