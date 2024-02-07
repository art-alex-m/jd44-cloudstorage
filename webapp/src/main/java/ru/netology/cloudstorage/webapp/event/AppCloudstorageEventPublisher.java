package ru.netology.cloudstorage.webapp.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.handler.CloudstorageEventPublisher;
import ru.netology.cloudstorage.contracts.event.model.CloudstorageEvent;

@Component("appCloudstorageEventPublisher")
public class AppCloudstorageEventPublisher implements CloudstorageEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public AppCloudstorageEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean publish(CloudstorageEvent event) throws CloudstorageEventPublisherException {
        eventPublisher.publishEvent(event);
        return true;
    }
}
