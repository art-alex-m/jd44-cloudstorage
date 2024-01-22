package ru.netology.cloudstorage.contracts.event.model;

import ru.netology.cloudstorage.contracts.trace.model.Traceable;

import java.time.Instant;

public interface CloudstorageEvent extends Traceable {
    Instant getCreatedAt();
}
