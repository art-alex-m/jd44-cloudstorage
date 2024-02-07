package ru.netology.cloudstorage.contracts.core.model;

import java.util.UUID;

public interface TraceId {
    long getId();

    UUID getUuid();
}
