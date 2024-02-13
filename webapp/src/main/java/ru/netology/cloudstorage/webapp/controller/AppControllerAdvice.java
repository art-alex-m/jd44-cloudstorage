package ru.netology.cloudstorage.webapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdContainer;
import ru.netology.cloudstorage.webapp.model.AppError;

@RestControllerAdvice
@AllArgsConstructor
public class AppControllerAdvice {

    private TraceIdContainer traceIdContainer;

    @ExceptionHandler({AuthenticationException.class, CloudFileException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleBadRequestExceptions(Exception ex) {
        return makeBaseAppError(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException ex) {
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        return makeBaseAppError(String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order
    public AppError handleAllExceptions(Exception ex) {
        return makeBaseAppError(ex);
    }

    private AppError makeBaseAppError(Exception ex) {
        return makeBaseAppError(ex.getMessage());
    }

    private AppError makeBaseAppError(String message) {
        return new AppError(traceIdContainer.getTraceId().getId(), message);
    }
}
