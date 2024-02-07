package ru.netology.cloudstorage.contracts.core.boundary.delete;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

/**
 * Сценарий "Удаление файла"
 * Шаг 1. Удаление записи о файле хранилища StorageFile из базы данных
 */
public interface DeleteCloudFileInput {
    /**
     * Удаление файла пользователя
     *
     * @param request DeleteCloudFileInputRequest
     * @return DeleteCloudFileInputResponse
     */
    DeleteCloudFileInputResponse delete(DeleteCloudFileInputRequest request) throws CloudFileException;
}
