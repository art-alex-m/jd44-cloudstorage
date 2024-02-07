package ru.netology.cloudstorage.core.model;


import ru.netology.cloudstorage.contracts.core.model.TraceId;

import java.util.UUID;

public record CoreTraceId(long id, UUID uuid) implements TraceId {
    @Override
    public long getId() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}
