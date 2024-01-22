package ru.netology.cloudstorage.core.factory;

import lombok.Getter;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.core.model.CoreTraceId;

import java.time.Instant;
import java.util.UUID;

@Getter
public class CloudFileTestDataFactory {
    private final UUID testUuid = UUID.fromString("6040a95b-8787-4bdb-b711-36d9d413be5c");

    private final TraceId traceId = new CoreTraceId(4915623779632245515L, testUuid);

    private final String testFileName = "some test name.pdf";

    private final Instant testInstant = Instant.now();
}
