package ru.netology.cloudstorage.contracts.storage.repository;


import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.storage.exception.CloudstorageStorageException;

/**
 * Репозиторий в сценарии "Сохранение файла"
 */
public interface CreateCloudFileStorageUploadRepository {
    /**
     * Сохраняет файл в файловое хранилище
     *
     * @param resource FileResource
     * @return StorageFile
     * @throws CloudstorageStorageException если возникли ошибки при сохранении файлы
     */
    StorageFile save(FileResource resource) throws CloudstorageStorageException;
}
