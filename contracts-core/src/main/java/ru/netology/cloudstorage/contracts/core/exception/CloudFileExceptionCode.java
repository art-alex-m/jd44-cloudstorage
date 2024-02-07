package ru.netology.cloudstorage.contracts.core.exception;

public enum CloudFileExceptionCode {
    DB_CREATE_ERROR("cloudstorage.contracts.core.exception.db-create-error"),
    UPLOAD_ERROR("cloudstorage.contracts.core.exception.upload-error"),
    DB_SAVE_STORAGE_FILE_ERROR("cloudstorage.contracts.core.exception.db-save-storage-file-error"),
    DB_SAVE_IS_READY_ERROR("cloudstorage.contracts.core.exception.db-save-is-ready-error"),
    DB_SAVE_STATUS_ERROR("cloudstorage.contracts.core.exception.db-save-status-error"),
    DB_UPDATE_ERROR("cloudstorage.contracts.core.exception.db-update-error"),
    NO_UNIQUE_FILE_NAME_ERROR("cloudstorage.contracts.core.exception.no-unique-file-name-error"),
    FILE_NOT_FOUND_ERROR("cloudstorage.contracts.core.exception.file-not-found-error"),
    DB_STORAGE_FILE_DELETE_ERROR("cloudstorage.contracts.core.exception.db-storage-file-delete-error"),
    STORAGE_FILE_DELETE_ERROR("cloudstorage.contracts.core.exception.storage-real-file-delete-error"),
    CLOUD_FILE_DELETE_ERROR("cloudstorage.contracts.core.exception.cloud-file-delete-error");

    public final String code;

    CloudFileExceptionCode(String code) {
        this.code = code;
    }
}
