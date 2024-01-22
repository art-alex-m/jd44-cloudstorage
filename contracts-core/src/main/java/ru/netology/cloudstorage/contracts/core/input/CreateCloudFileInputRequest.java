package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.storage.model.FileResource;
import ru.netology.cloudstorage.contracts.trace.model.Traceable;


public interface CreateCloudFileInputRequest extends Traceable {
    CloudUser getUser();

    FileResource getUserFile();
}
