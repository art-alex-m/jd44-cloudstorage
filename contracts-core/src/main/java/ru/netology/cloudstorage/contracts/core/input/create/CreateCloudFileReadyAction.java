package ru.netology.cloudstorage.contracts.core.input.create;

public interface CreateCloudFileReadyAction {
    boolean update(CreateCloudFileReadyActionRequest request);
}
