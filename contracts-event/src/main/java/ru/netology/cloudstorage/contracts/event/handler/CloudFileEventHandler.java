package ru.netology.cloudstorage.contracts.event.handler;

import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;

public interface CloudFileEventHandler {
    boolean handle(CloudFileEvent event);
}
