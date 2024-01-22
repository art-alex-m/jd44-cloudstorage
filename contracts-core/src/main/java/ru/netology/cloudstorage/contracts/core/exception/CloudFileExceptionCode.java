package ru.netology.cloudstorage.contracts.core.exception;

public enum CloudFileExceptionCode {
    DB_CREATE_ERROR("cloudstorage.contracts.core.exception.db-create-error"),
    UPLOAD_ERROR("cloudstorage.contracts.core.exception.upload-error"),
    DB_SAVE_STORAGE_FILE_ERROR("cloudstorage.contracts.core.exception.db-save-storage-file-error"),
    DB_SAVE_IS_READY_ERROR("cloudstorage.contracts.core.exception.db-save-is-ready-error"),
    DB_SAVE_STATUS_ERROR("cloudstorage.contracts.core.exception.db-save-status-error");

    public final String code;

    CloudFileExceptionCode(String code) {
        this.code = code;
    }
}
