package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;

import java.util.List;

/**
 * Репозиторий сценария "Просмотр списка сохраненных файлов" для связи с БД
 */
public interface ListCloudFileInputDbRepository {
    /**
     * Список файлов пользователя со статусом "READY"
     *
     * @param user  CloudUser
     * @param limit int
     * @return List<CloudFile>
     */
    List<CloudFile> findByUserAndReadyStatus(CloudUser user, int limit);
}
