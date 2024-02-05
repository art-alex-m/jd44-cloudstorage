package ru.netology.cloudstorage.core.event.update;

import lombok.experimental.SuperBuilder;
import ru.netology.cloudstorage.contracts.event.model.update.CloudFileUpdated;

@SuperBuilder
public class CoreCloudFileUpdated extends CoreUpdateCloudFileEvent implements CloudFileUpdated {
}
