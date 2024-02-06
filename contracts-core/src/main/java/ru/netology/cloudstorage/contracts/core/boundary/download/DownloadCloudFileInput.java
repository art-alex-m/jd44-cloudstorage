package ru.netology.cloudstorage.contracts.core.boundary.download;

import ru.netology.cloudstorage.contracts.core.model.FileResource;

/**
 * Сценарий "Скачивание файла"
 */
public interface DownloadCloudFileInput {
    /**
     * Получение объекта для скачивания файла
     *
     * @param request DownloadCloudFileInputRequest
     * @return FileResource
     */
    FileResource getResource(DownloadCloudFileInputRequest request);
}
