package ru.netology.cloudstorage.contracts.core.boundary;

import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

public interface CloudFileInputRequest extends Traceable {
    CloudUser getUser();
}
