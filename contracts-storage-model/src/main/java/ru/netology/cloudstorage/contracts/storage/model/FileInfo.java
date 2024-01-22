package ru.netology.cloudstorage.contracts.storage.model;

import ru.netology.cloudstorage.contracts.base.model.Timestamps;

public interface FileInfo extends Timestamps {
    long getSize();

    String getFileName();

    String getMediaType();
}
