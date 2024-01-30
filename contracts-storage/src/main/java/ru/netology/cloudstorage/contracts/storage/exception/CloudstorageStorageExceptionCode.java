package ru.netology.cloudstorage.contracts.storage.exception;

public enum CloudstorageStorageExceptionCode {
    STORAGE_SAVE_ERROR("cloudstorage.contracts.storage.exception.storage-save-error");

    public final String code;

    CloudstorageStorageExceptionCode(String code) {
        this.code = code;
    }
}
