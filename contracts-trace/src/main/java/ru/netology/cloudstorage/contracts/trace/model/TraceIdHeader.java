package ru.netology.cloudstorage.contracts.trace.model;

public interface TraceIdHeader {
    String ID = "X-Trace-Id";

    String UUID = "X-Trace-Uuid";

    String TRACE_ID = "ru.netology.cloudstorage.contracts.trace.model.TraceId";
}
