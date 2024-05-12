package ru.netology.cloudstorage.contracts.trace.model;

public final class TraceIdHeader {
    public static final String ID = "X-Trace-Id";
    public static final String UUID = "X-Trace-Uuid";
    public static final String TRACE_ID = "ru.netology.cloudstorage.contracts.trace.model.TraceId";

    private TraceIdHeader() {}
}
