package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

public interface CreateCloudFileInputRequest extends Traceable {
    CloudUser getUser();

    FileResource getUserFile();
}
