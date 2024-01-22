package ru.netology.cloudstorage.contracts.trace.model;


import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.core.model.Traceable;

public interface TraceIdContainer extends Traceable {
    void setTraceId(TraceId traceId);
}
