package ru.netology.cloudstorage.contracts.core.factory;

import java.util.UUID;

public interface CloudFileIdFactory {
    UUID create(UUID namespace, String name);
}
