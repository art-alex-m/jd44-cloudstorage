package ru.netology.cloudstorage.webapp.event.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileAction;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.delete.StorageFileDeleted;

@RequiredArgsConstructor
@Component
public class AppStorageDeletedToCloudFileDeleteHandler implements CloudFileEventHandler {

    private final DeleteCloudFileAction deleteCloudFileAction;

    @Async
    @EventListener(StorageFileDeleted.class)
    public void handleAsync(CloudFileEvent event) {
        handle(event);
    }

    @Override
    public boolean handle(CloudFileEvent event) {
        return deleteCloudFileAction.delete((DeleteCloudFileActionRequest) event);
    }
}
