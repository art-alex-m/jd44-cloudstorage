package ru.netology.cloudstorage.contracts.core.boundary.update;

/**
 * Сценарий "Изменение имени файла"
 */
public interface UpdateCloudFileInput {
    /**
     * Обновление информации о файле пользователя
     *
     * @param request UpdateCloudFileInputRequest
     * @return UpdateCloudFileInputResponse
     */
    UpdateCloudFileInputResponse update(UpdateCloudFileInputRequest request);
}
