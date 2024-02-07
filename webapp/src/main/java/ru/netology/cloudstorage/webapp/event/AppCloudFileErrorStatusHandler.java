package ru.netology.cloudstorage.webapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusAction;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;
import ru.netology.cloudstorage.contracts.event.model.delete.DeleteCloudFileError;

@Component
@RequiredArgsConstructor
public class AppCloudFileErrorStatusHandler implements CloudFileEventHandler {

    private final CloudFileErrorStatusAction errorStatusAction;

    @Async
    @EventListener({CreateCloudFileError.class, DeleteCloudFileError.class})
    public void handleAsync(CloudFileEvent event) {
        handle(event);
    }

    @Override
    public boolean handle(CloudFileEvent event) {
        return errorStatusAction.update((CloudFileErrorStatusActionRequest) event);
    }
}
