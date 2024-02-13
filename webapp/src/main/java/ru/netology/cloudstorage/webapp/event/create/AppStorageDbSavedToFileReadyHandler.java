package ru.netology.cloudstorage.webapp.event.create;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileReadyActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileDbStored;

@Component
@RequiredArgsConstructor
public class AppStorageDbSavedToFileReadyHandler implements CloudFileEventHandler {

    private final CreateCloudFileReadyAction fileReadyAction;

    @Async
    @EventListener(StorageFileDbStored.class)
    public void handleAsync(CloudFileEvent event) {
        handle(event);
    }

    @Override
    public boolean handle(CloudFileEvent event) {
        return fileReadyAction.update((CreateCloudFileReadyActionRequest) event);
    }
}
