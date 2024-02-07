package ru.netology.cloudstorage.contracts.core.boundary.list;

/**
 * Сценарий "Просмотр списка сохраненных файлов"
 */
public interface ListCloudFileInput {
    /**
     * Возвращает список файлов пользователя
     *
     * @param request ListCloudFileInputRequest
     * @return ListCloudFileInputResponse
     */
    ListCloudFileInputResponse find(ListCloudFileInputRequest request);
}
