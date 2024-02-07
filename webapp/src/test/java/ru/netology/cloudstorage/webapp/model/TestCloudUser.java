package ru.netology.cloudstorage.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.CloudUser;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TestCloudUser implements CloudUser {
    private final UUID id;

    public TestCloudUser() {
        this.id = UUID.randomUUID();
    }
}
