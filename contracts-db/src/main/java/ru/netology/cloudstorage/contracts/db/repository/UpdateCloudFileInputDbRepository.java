package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;

import java.util.Optional;

/**
 * Репозиторий сценария "Изменение имени файла" для связи с БД
 */
public interface UpdateCloudFileInputDbRepository {
    /**
     * Находит файл по имени
     *
     * @param user     CloudUser
     * @param fileName String
     * @return CloudFile
     */
    Optional<CloudFile> findByUserAndFileName(CloudUser user, String fileName);

    /**
     * Проверяет, что новое имя файла уникально для пользователя
     *
     * @param user     CloudUser
     * @param fileName String
     * @return Истина, если имя доступно
     */
    boolean uniqueNewName(CloudUser user, String fileName);

    /**
     * Обновляет измененный файл
     *
     * @param cloudFile CloudFile
     * @return Истина, кода файл обновлен
     */
    boolean update(CloudFile cloudFile);
}
