package ru.netology.cloudstorage.webapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Представление ошибки для api
 */
@AllArgsConstructor
@Getter
public class AppError {
    @JsonProperty("id")
    private long traceId;

    private String message;
}
