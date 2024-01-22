package ru.netology.cloudstorage.contracts.event.handler;

import ru.netology.cloudstorage.contracts.event.exception.CloudstorageEventPublisherException;
import ru.netology.cloudstorage.contracts.event.model.CloudstorageEvent;

public interface CloudstorageEventPublisher {
    boolean publish(CloudstorageEvent event) throws CloudstorageEventPublisherException;
}
