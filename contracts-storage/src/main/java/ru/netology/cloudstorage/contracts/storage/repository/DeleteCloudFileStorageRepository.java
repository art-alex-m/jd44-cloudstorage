package ru.netology.cloudstorage.contracts.storage.repository;

import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.contracts.storage.exception.CloudstorageStorageException;

/**
 * Репозиторий для удаления файла в сценарии "Удаление файла"
 */
public interface DeleteCloudFileStorageRepository {
    /**
     * Удаляет файл из хранилища
     *
     * @param storageFile StorageFile
     * @return Истина, если файл удален
     * @throws CloudstorageStorageException если файл не может быть удален
     */
    boolean delete(StorageFile storageFile) throws CloudstorageStorageException;
}
