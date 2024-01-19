package ru.netology.cloudstorage.contracts.core.model;

import java.time.Instant;

public interface Timestamps {
    Instant getCreatedAt();

    Instant getUpdatedAt();
}
