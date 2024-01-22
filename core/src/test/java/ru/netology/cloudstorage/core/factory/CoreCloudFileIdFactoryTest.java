package ru.netology.cloudstorage.core.factory;

import org.junit.jupiter.api.Test;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileIdFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CoreCloudFileIdFactoryTest {

    @Test
    void create() {
        String name = "NameForTest";
        UUID namespace1 = UUID.randomUUID();
        UUID namespace2 = UUID.randomUUID();
        CloudFileIdFactory sut = new CoreCloudFileIdFactory();

        UUID result1 = sut.create(namespace1, name);
        UUID result2 = sut.create(namespace2, name);

        assertNotEquals(result1, result2);
    }
}
