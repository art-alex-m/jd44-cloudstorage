package ru.netology.cloudstorage.contracts.core.boundary.delete;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

/**
 * Сценарий "Удаление файла"
 * Шаг 2. Действие удаления файла из физического хранилища
 */
public interface DeleteCloudFileStorageDeleteAction {
    /**
     * Удаляет файл из физического хранилища
     *
     * @param request DeleteCloudFileStorageDeleteActionRequest
     * @return Истина, когда файл успешно удален
     */
    boolean delete(DeleteCloudFileStorageDeleteActionRequest request) throws CloudFileException;
}
