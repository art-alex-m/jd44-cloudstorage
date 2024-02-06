package ru.netology.cloudstorage.contracts.storage.repository;

import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

/**
 * Хранилище файлов для сценария "Скачивание файла"
 */
public interface DownloadCloudFileStorageRepository {
    /**
     * Возвращает файловый ресурс с InputStream
     *
     * @param storageFile StorageFile
     * @return FileResource
     */
    FileResource getResource(StorageFile storageFile);
}
