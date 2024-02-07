package ru.netology.cloudstorage.contracts.core.boundary;

import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;

/**
 * Обновляет статус файла при ошибках
 */
public interface CloudFileErrorStatusAction {
    /**
     * Создает статус "Ошибка"
     *
     * @param request CloudFileErrorStatusActionRequest
     * @return boolean
     * @throws CloudFileException если возникает ошибка
     */
    boolean update(CloudFileErrorStatusActionRequest request) throws CloudFileException;
}
