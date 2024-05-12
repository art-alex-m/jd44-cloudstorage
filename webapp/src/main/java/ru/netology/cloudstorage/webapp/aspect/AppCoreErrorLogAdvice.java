package ru.netology.cloudstorage.webapp.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.contracts.core.model.Traceable;
import ru.netology.cloudstorage.webapp.service.AppTraceIdMDCService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AppCoreErrorLogAdvice {

    private final AppTraceIdMDCService mdcService;

    private final ObjectMapper objectMapper;

    @Pointcut("within(ru.netology.cloudstorage.core.boundary..*)")
    public void coreInteractors() {
    }

    @Pointcut("execution(public * *(..))")
    public void anyPublicMethod() {
    }

    @AfterThrowing("anyPublicMethod() && coreInteractors() && args(request, ..)")
    public void logMethodParams(JoinPoint joinPoint, Object request) {
        if (request instanceof Traceable traceable) {
            mdcService.put(traceable.getTraceId());
        }
        String message;
        try {
            message = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException ex) {
            log.error("Parse args error", ex);
            message = request.toString();
        }
        log.error("Exception in method '{}' called with params: '{}'", joinPoint.getSignature().toLongString(),
                message);
    }
}
