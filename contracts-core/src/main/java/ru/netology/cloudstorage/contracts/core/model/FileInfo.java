package ru.netology.cloudstorage.contracts.core.model;

public interface FileInfo extends Timestamps {
    long getSize();

    String getFileName();
}
