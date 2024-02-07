package ru.netology.cloudstorage.webapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusAction;
import ru.netology.cloudstorage.contracts.core.boundary.CloudFileErrorStatusActionRequest;
import ru.netology.cloudstorage.contracts.event.handler.CloudFileEventHandler;
import ru.netology.cloudstorage.contracts.event.model.CloudFileEvent;
import ru.netology.cloudstorage.contracts.event.model.create.CreateCloudFileError;

@Component
@RequiredArgsConstructor
public class AppCloudFileErrorStatusHandler implements CloudFileEventHandler {

    private final CloudFileErrorStatusAction errorStatusAction;

    @Override
    @EventListener({CreateCloudFileError.class})
    public boolean handle(CloudFileEvent event) {
        return errorStatusAction.update((CloudFileErrorStatusActionRequest) event);
    }
}
