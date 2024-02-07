package ru.netology.cloudstorage.contracts.db.repository;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;

/**
 * Репозиторий сценария "Удаление файла"
 */
public interface DeleteCloudFileActionDbRepository {
    /**
     * Удаляет всю информацию о файле из хранилища
     *
     * @param cloudFile CloudFile
     * @return Истина
     */
    boolean delete(CloudFile cloudFile);
}
