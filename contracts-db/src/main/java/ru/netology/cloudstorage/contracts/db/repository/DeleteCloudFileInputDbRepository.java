package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;

import java.util.Optional;

/**
 * Репозиторий сценария "Удаление файла" для поиска файла в базе данных
 */
public interface DeleteCloudFileInputDbRepository {
    /**
     * Находит облачный файл по имени
     *
     * @param user     CloudUser
     * @param fileName String
     * @return CloudFile
     */
    Optional<CloudFile> findByUserAndFileNameAndReadyStatus(CloudUser user, String fileName);

    /**
     * Удаляет из базы данных запись о файле хранилища
     *
     * @param storageFile StorageFile
     * @return Истина, если файл удален
     */
    boolean delete(StorageFile storageFile);

    /**
     * Обновляет измененный облачный файл
     *
     * @param cloudFile CloudFile
     * @return Истина, кода файл обновлен
     */
    boolean update(CloudFile cloudFile);
}
