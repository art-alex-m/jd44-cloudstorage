package ru.netology.cloudstorage.contracts.core.model;

import ru.netology.cloudstorage.contracts.base.model.Timestamps;

import java.util.UUID;

public interface CloudFileStatus extends Timestamps {
    UUID getId();

    CloudFileStatusCode getCode();

    String getMessage();
}
