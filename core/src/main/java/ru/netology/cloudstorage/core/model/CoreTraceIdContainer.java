package ru.netology.cloudstorage.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdContainer;


@AllArgsConstructor
@Getter
@Setter
public class CoreTraceIdContainer implements TraceIdContainer {
    private TraceId traceId;
}
