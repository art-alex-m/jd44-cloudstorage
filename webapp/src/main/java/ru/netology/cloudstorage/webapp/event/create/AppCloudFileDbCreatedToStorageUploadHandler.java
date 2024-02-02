package ru.netology.cloudstorage.webapp.event.create;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageUploadActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileDbCreated;

@Component
@RequiredArgsConstructor
public class AppCloudFileDbCreatedToStorageUploadHandler implements CloudFileEventHandler {

    private final CreateCloudFileStorageUploadAction fileStorageUploadAction;

    @Override
    @EventListener(CloudFileDbCreated.class)
    public boolean handle(CloudFileEvent event) {
        return fileStorageUploadAction.upload((CreateCloudFileStorageUploadActionRequest) event);
    }
}
