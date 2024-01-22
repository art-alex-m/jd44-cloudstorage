package ru.netology.cloudstorage.contracts.base.model;

import java.time.Instant;

public interface Timestamps {
    Instant getCreatedAt();

    Instant getUpdatedAt();
}
