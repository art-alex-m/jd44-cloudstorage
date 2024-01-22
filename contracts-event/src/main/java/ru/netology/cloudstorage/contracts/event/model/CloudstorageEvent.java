package ru.netology.cloudstorage.contracts.event.model;

import ru.netology.cloudstorage.contracts.core.model.Traceable;

import java.time.Instant;

public interface CloudstorageEvent extends Traceable {
    Instant getCreatedAt();
}
