package ru.netology.cloudstorage.core.event.delete;

import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.event.model.delete.CloudFileDeleted;

@SuperBuilder
public class CoreCloudFileDeleted extends CoreDeleteCloudFileEvent implements CloudFileDeleted {
}
