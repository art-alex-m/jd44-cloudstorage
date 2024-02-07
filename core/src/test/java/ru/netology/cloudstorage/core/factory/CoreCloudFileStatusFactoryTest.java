package ru.netology.cloudstorage.core.factory;

import org.junit.jupiter.api.Test;
import ru.netology.cloudstorage.contracts.core.factory.CloudFileStatusFactory;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CoreCloudFileStatusFactoryTest {

    private final TraceIdFactory traceIdFactory = new CoreTraceIdFactory();

    @Test
    void create() {
        TraceId traceId = traceIdFactory.create();
        CloudFileStatusFactory sut = new CoreCloudFileStatusFactory();

        CloudFileStatus result = sut.create(CloudFileStatusCode.LOADING, traceId);

        assertEquals(CloudFileStatusCode.LOADING, result.getCode());
        assertNull(result.getMessage());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertEquals(traceId, result.getTraceId());
    }
}
