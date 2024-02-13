package ru.netology.cloudstorage.storage.local.repository;

import lombok.RequiredArgsConstructor;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.storage.exception.CloudstorageStorageException;
import ru.netology.cloudstorage.contracts.storage.exception.CloudstorageStorageExceptionCode;
import ru.netology.cloudstorage.contracts.storage.repository.CreateCloudFileStorageUploadRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DeleteCloudFileStorageRepository;
import ru.netology.cloudstorage.contracts.storage.repository.DownloadCloudFileStorageRepository;
import ru.netology.cloudstorage.storage.local.model.LocalFileResource;
import ru.netology.cloudstorage.storage.local.model.LocalStorageFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RequiredArgsConstructor
public class LocalStorageFileRepository implements CreateCloudFileStorageUploadRepository,
        DownloadCloudFileStorageRepository, DeleteCloudFileStorageRepository {

    private final Path basePath;

    @Override
    public StorageFile save(FileResource resource) throws CloudstorageStorageException {
        try {
            UUID fileName = UUID.randomUUID();
            Files.copy(resource.getInputStream(), getStoragePath(fileName.toString()));
            return LocalStorageFile.builder()
                    .size(resource.getSize())
                    .mediaType(resource.getMediaType())
                    .id(fileName)
                    .fileName(fileName.toString())
                    .build();
        } catch (Exception ex) {
            throw new CloudstorageStorageException(CloudstorageStorageExceptionCode.STORAGE_SAVE_ERROR, ex);
        }
    }

    @Override
    public FileResource getResource(StorageFile storageFile) {
        return new LocalFileResource(storageFile, getStoragePath(storageFile.getFileName()));
    }

    @Override
    public boolean delete(StorageFile storageFile) throws CloudstorageStorageException {
        try {
            Path storagePath = getStoragePath(storageFile.getFileName());
            Files.deleteIfExists(storagePath);
            return true;
        } catch (Exception ex) {
            throw new CloudstorageStorageException(CloudstorageStorageExceptionCode.STORAGE_DELETE_ERROR, ex);
        }
    }

    public Path getStoragePath(String fileName) {
        return Path.of(basePath.toString(), fileName);
    }
}
