package ru.netology.cloudstorage.contracts.core.boundary.create;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

/**
 * Сценарий "Сохранение файла"
 * Шаг 1. Создание записи об облачном файле в базе данных: CloudFile, CloudFileStatus:LOADING
 */
public interface CreateCloudFileInput {
    /**
     * Создает запись об облачном файле в базе данных
     *
     * @param request CreateCloudFileInputRequest
     * @return CreateCloudFileInputResponse
     * @throws CloudFileException Если возникли исключения
     */
    CreateCloudFileInputResponse create(CreateCloudFileInputRequest request) throws CloudFileException;
}
