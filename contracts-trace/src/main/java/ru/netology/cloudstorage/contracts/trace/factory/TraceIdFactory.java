package ru.netology.cloudstorage.contracts.trace.factory;


import ru.netology.cloudstorage.contracts.trace.model.TraceId;

import java.util.UUID;

public interface TraceIdFactory {
    TraceId create();

    TraceId create(String uuid);

    long create(UUID id);
}
