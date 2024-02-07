package ru.netology.cloudstorage.webapp.event.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileStorageDeleteActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDbDeleted;

@Component
@RequiredArgsConstructor
public class AppStorageFileDbDeletedToStorageDeleteHandler implements CloudFileEventHandler {

    private final DeleteCloudFileStorageDeleteAction storageDeleteAction;

    @Async
    @EventListener(StorageFileDbDeleted.class)
    public void handleAsync(CloudFileEvent event) {
        handle(event);
    }

    @Override
    public boolean handle(CloudFileEvent event) {
        return storageDeleteAction.delete((DeleteCloudFileStorageDeleteActionRequest) event);
    }
}
