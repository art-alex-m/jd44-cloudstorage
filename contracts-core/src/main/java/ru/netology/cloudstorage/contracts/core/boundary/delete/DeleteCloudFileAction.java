package ru.netology.cloudstorage.contracts.core.boundary.delete;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

/**
 * Сценарий "Удаление файла"
 * Шаг 3. Действие удаления всей информации из базы данных: CloudFile, CloudFileStatus и др
 */
public interface DeleteCloudFileAction {
    /**
     * Удаляет информацию об облачном файле их базы данных
     *
     * @param request DeleteCloudFileActionRequest
     * @return Истина, если информация удалена из базы данных
     */
    boolean delete(DeleteCloudFileActionRequest request) throws CloudFileException;
}
