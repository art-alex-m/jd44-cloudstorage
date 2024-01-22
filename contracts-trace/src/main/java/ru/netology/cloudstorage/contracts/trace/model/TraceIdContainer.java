package ru.netology.cloudstorage.contracts.trace.model;

public interface TraceIdContainer extends Traceable {
    void setTraceId(TraceId traceId);
}
