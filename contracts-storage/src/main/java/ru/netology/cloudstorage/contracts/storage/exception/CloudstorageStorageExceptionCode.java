package ru.netology.cloudstorage.contracts.storage.exception;

public enum CloudstorageStorageExceptionCode {
    STORAGE_SAVE_ERROR("cloudstorage.contracts.storage.exception.storage-save-error"),
    STORAGE_DELETE_ERROR("cloudstorage.contracts.storage.exception.storage-delete-error");

    public final String code;

    CloudstorageStorageExceptionCode(String code) {
        this.code = code;
    }
}
