package ru.netology.cloudstorage.core.factory;

import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceId;
import ru.netology.cloudstorage.core.model.CoreTraceId;

import java.util.UUID;

/**
 * CoreTraceIdFactory
 *
 * <p>
 * <a href="https://www.baeldung.com/java-uuid-unique-long-generation">Generating Unique Positive long Using UUID in Java</a><br>
 * </p>
 */
public class CoreTraceIdFactory implements TraceIdFactory {
    @Override
    public TraceId create() {
        UUID id = UUID.randomUUID();
        return new CoreTraceId(create(id), id);
    }

    @Override
    public TraceId create(String uuid) {
        UUID id = UUID.fromString(uuid);
        return new CoreTraceId(create(id), id);
    }

    @Override
    public long create(UUID id) {
        long mostSignificantBits = id.getMostSignificantBits();
        long leastSignificantBits = id.getLeastSignificantBits();
        return Math.abs(mostSignificantBits ^ (leastSignificantBits >> 1));
    }
}
