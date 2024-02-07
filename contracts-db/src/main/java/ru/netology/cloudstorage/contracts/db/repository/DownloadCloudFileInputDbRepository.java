package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;

import java.util.Optional;

/**
 * Репозиторий сценария "Скачивание файла"
 */
public interface DownloadCloudFileInputDbRepository {
    /**
     * Находит файл по имени
     *
     * @param user     CloudUser
     * @param fileName String
     * @return CloudFile
     */
    Optional<CloudFile> findByUserAndFileNameAndReadyStatus(CloudUser user, String fileName);
}
