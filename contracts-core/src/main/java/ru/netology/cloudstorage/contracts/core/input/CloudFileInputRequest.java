package ru.netology.cloudstorage.contracts.core.input;

import ru.netology.cloudstorage.contracts.core.model.CloudUser;
import ru.netology.cloudstorage.contracts.trace.model.Traceable;

public interface CloudFileInputRequest extends Traceable {
    CloudUser getUser();
}
