package ru.netology.cloudstorage.contracts.core.model;

import java.util.UUID;

public interface CloudFileStatus extends Timestamps, Traceable {
    UUID getId();

    CloudFileStatusCode getCode();

    String getMessage();
}
