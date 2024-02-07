package ru.netology.cloudstorage.webapp.service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdContainer;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdHeader;

/**
 * AppTraceIdInterceptor
 * <p>
 * TODO: See difference between Filters and HandlerInterceptor
 * </p>
 * <p>
 * <a href="https://blogs.ashrithgn.com/adding-transaction-id-or-trace-id-for-each-request-in-spring-boot/">Adding transaction id or trace Id for each request in spring boot</a><br>
 * </p>
 */
@Component
public class AppTraceIdInterceptor implements HandlerInterceptor {

    private final TraceIdFactory traceIdFactory;
    @Resource
    private TraceIdContainer traceIdContainer;

    public AppTraceIdInterceptor(TraceIdFactory traceIdFactory) {
        this.traceIdFactory = traceIdFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        TraceId traceId = traceIdContainer.getTraceId();

        String uuid = request.getHeader(TraceIdHeader.UUID);
        if (uuid != null && !uuid.isEmpty()) {
            traceId = traceIdFactory.create(uuid);
            traceIdContainer.setTraceId(traceId);
        }

        response.setHeader(TraceIdHeader.ID, String.valueOf(traceId.getId()));
        response.setHeader(TraceIdHeader.UUID, traceId.getUuid().toString());

        return true;
    }
}
