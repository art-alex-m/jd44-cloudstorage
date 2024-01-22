package ru.netology.cloudstorage.contracts.event.input;

import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;

public interface CloudFileEventStoreInput {
    boolean store(CloudFileEvent event);
}
