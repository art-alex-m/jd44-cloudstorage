package ru.netology.cloudstorage.contracts.core.exception;

import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

public class CloudFileException extends CloudstorageException implements Traceable {
    private final TraceId traceId;

    public CloudFileException(CloudFileExceptionCode message, TraceId traceId) {
        super(message.code);
        this.traceId = traceId;
    }

    public CloudFileException(CloudFileExceptionCode message, TraceId traceId, Throwable cause) {
        super(message.code, cause);
        this.traceId = traceId;
    }

    @Override
    public TraceId getTraceId() {
        return traceId;
    }
}
