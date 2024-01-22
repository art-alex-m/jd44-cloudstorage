package ru.netology.cloudstorage.contracts.core.boundary;

import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

public interface CloudFileActionRequest extends Traceable {
    CloudFile getCloudFile();
}
