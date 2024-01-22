package ru.netology.cloudstorage.contracts.event.boundary;

import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;

public interface CloudFileEventStoreInput {
    boolean store(CloudFileEvent event);
}
