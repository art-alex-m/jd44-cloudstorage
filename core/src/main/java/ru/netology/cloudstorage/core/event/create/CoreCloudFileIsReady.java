package ru.netology.cloudstorage.core.event.create;

import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.event.model.create.CloudFileIsReady;

@SuperBuilder
public class CoreCloudFileIsReady extends CoreCreateCloudFileEvent implements CloudFileIsReady {
}
