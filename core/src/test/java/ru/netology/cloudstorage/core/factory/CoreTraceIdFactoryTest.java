package ru.netology.cloudstorage.core.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceId;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CoreTraceIdFactoryTest {

    public static Stream<Arguments> createFromStringUuid() {
        return Stream.of(
                Arguments.of("6040a95b-8787-4bdb-b711-36d9d413be5c", 4915623779632245515L)
        );
    }

    @Test
    void create() {
        TraceIdFactory sut = new CoreTraceIdFactory();

        TraceId result = sut.create();

        assertNotNull(result);
        assertInstanceOf(UUID.class, result.getUuid());
        assertInstanceOf(Long.class, result.getId());
        assertTrue(result.getId() > 0);
    }

    @ParameterizedTest
    @MethodSource
    void createFromStringUuid(String uuid, long expectedLongId) {
        TraceIdFactory sut = new CoreTraceIdFactory();

        TraceId result = sut.create(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getUuid().toString());
        assertEquals(expectedLongId, result.getId());
    }

    @ParameterizedTest
    @MethodSource("createFromStringUuid")
    void createLong(String uuid, long expectedLongId) {
        UUID id = UUID.fromString(uuid);
        TraceIdFactory sut = new CoreTraceIdFactory();

        long result = sut.create(id);

        assertTrue(result > 0L);
        assertEquals(expectedLongId, result);
    }
}