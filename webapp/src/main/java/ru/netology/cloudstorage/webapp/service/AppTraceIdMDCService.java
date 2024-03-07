package ru.netology.cloudstorage.webapp.service;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.TraceId;

@Component
public class AppTraceIdMDCService {

    public static final String TRACE_ID = "TraceId";

    public static final String TRACE_UUID = "TraceUuid";

    public void put(TraceId traceId) {
        MDC.put(TRACE_ID, String.valueOf(traceId.getId()));
        MDC.put(TRACE_UUID, traceId.getUuid().toString());
    }

    public void clear() {
        MDC.remove(TRACE_ID);
        MDC.remove(TRACE_UUID);
    }
}
