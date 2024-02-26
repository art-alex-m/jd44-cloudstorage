package ru.netology.cloudstorage.webapp.service;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
 * </p>
 */
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

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
