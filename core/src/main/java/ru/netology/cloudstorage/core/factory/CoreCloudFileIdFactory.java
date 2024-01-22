package ru.netology.cloudstorage.core.factory;

import ru.netology.cloudstorage.contracts.core.factory.CloudFileIdFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CoreCloudFileIdFactory implements CloudFileIdFactory {
    @Override
    public UUID create(UUID namespace, String name) {

        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(nameBytes.length + 16);
        buffer.putLong(namespace.getMostSignificantBits());
        buffer.putLong(namespace.getLeastSignificantBits());
        buffer.put(nameBytes);

        return UUID.nameUUIDFromBytes(buffer.array());
    }
}
