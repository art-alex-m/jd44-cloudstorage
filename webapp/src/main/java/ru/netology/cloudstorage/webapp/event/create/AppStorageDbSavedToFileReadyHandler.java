package ru.netology.cloudstorage.webapp.event.create;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

    @Override
    @EventListener(StorageFileDbStored.class)
    public boolean handle(CloudFileEvent event) {
        return fileReadyAction.update((CreateCloudFileReadyActionRequest) event);
    }
}
