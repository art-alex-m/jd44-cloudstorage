package ru.netology.cloudstorage.webapp.event.create;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveAction;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileStorageDbSaveActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.create.StorageFileUploaded;

@Component
@RequiredArgsConstructor
public class AppFileUploadedToStorageDbSaveHandler implements CloudFileEventHandler {

    private final CreateCloudFileStorageDbSaveAction fileStorageDbSaveAction;

    @Override
    @EventListener(StorageFileUploaded.class)
    public boolean handle(CloudFileEvent event) {
        return fileStorageDbSaveAction.save((CreateCloudFileStorageDbSaveActionRequest) event);
    }
}
