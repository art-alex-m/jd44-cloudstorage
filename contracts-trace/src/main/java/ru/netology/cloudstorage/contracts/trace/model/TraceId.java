package ru.netology.cloudstorage.contracts.trace.model;

import java.util.UUID;

public interface TraceId {
    long getId();

    UUID getUuid();
}
