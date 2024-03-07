package ru.netology.cloudstorage.webapp.service;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdHeader;

import java.io.IOException;
import java.util.Optional;

/**
 * AppTraceIdFilter
 *
 * <p>
 * <a href="https://blogs.ashrithgn.com/adding-transaction-id-or-trace-id-for-each-request-in-spring-boot/">Adding transaction id or trace Id for each request in spring boot</a><br>
 * <a href="https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter">HandlerInterceptors vs. Filters in Spring MVC</a><br>
 * <br>
 * <a href="https://www.baeldung.com/spring-cloud-sleuth-get-trace-id">Get Current Trace ID in Spring Cloud Sleuth</a><br>
 * <a href="https://stacktobasics.com/adding-correlation-ids-to-easily-track-down-errors-spring-boot-3-edition">Adding Correlation IDs to Easily Track Down Errors - Spring Boot 3 Edition</a><br>
 * <a href="https://www.tutorialspoint.com/spring_boot/spring_boot_tracing_micro_service_logs.htm">Spring Boot - Tracing Micro Service Logs</a><br>
 * <a href="https://stackoverflow.com/questions/67984914/spanid-and-traceid-missing-in-spring-boot-logs">SpanID and TraceID missing in Spring Boot logs</a><br>
 * <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.log-format">Logging</a><br>
 * </p>
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE + 50)
@RequiredArgsConstructor
public class AppTraceIdFilter implements Filter {

    private final TraceIdFactory traceIdFactory;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        TraceId traceId = Optional.ofNullable(request.getHeader(TraceIdHeader.UUID))
                .filter(uuid -> !uuid.isEmpty())
                .map(traceIdFactory::create)
                .orElse(traceIdFactory.create());

        request.setAttribute(TraceIdHeader.TRACE_ID, traceId);
        response.setHeader(TraceIdHeader.ID, String.valueOf(traceId.getId()));
        response.setHeader(TraceIdHeader.UUID, traceId.getUuid().toString());

        MDC.put("traceId", String.valueOf(traceId.getId()));
        MDC.put("traceUuid", traceId.getUuid().toString());

        log.debug("Tracing request {} {} with id {}", request.getMethod(), request.getRequestURI(), traceId.getId());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
