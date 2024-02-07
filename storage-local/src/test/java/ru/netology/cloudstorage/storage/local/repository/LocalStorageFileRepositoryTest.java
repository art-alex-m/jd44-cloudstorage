package ru.netology.cloudstorage.storage.local.repository;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.contracts.core.model.StorageFile;
import ru.netology.cloudstorage.storage.local.model.ClasspathFileResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalStorageFileRepositoryTest {

    private static final Path testFileName = Path.of("storage/test-file-resource.txt");

    private static final Path basePath = Path.of("storage/test");

    LocalStorageFileRepository sut;

    @BeforeEach
    void setUp() throws IOException {
        sut = new LocalStorageFileRepository(basePath);
        Files.createDirectories(basePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(basePath.toFile());
    }

    @Test
    void givenFileResource_whenSave_thenSuccess() throws IOException {
        FileResource testResource = new ClasspathFileResource(testFileName);

        StorageFile result = sut.save(testResource);

        assertNotNull(result);
        assertEquals(testResource.getSize(), result.getSize());
        assertTrue(result.getSize() > 0);
        assertEquals(testResource.getMediaType(), result.getMediaType());
        assertNotEquals(testResource.getFileName(), result.getFileName());
        assertNotNull(result.getId());
        assertNotNull(result.getFileName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertEquals(result.getId().toString(), result.getFileName());
        Path resultFile = Path.of(basePath.toString(), result.getFileName());
        assertTrue(resultFile.toFile().exists());
        assertEquals(Files.size(resultFile), result.getSize());
    }

    @Test
    void givenStorageFile_whenGetResource_thenSuccess() throws IOException {
        FileResource testResource = new ClasspathFileResource(testFileName);
        StorageFile storageFile = sut.save(testResource);

        FileResource result = sut.getResource(storageFile);

        assertNotNull(result);
        assertEquals(testResource.getMediaType(), result.getMediaType());
        assertEquals(testResource.getSize(), result.getSize());
        assertEquals(storageFile.getFileName(), result.getFileName());
        assertNotNull(result.getInputStream());
    }

    @Test
    void givenStorageFile_whenDelete_thenSuccess() {
        FileResource testResource = new ClasspathFileResource(testFileName);
        StorageFile storageFile = sut.save(testResource);
        Path storagePath = sut.getStoragePath(storageFile.getFileName());

        BooleanSupplier result = () -> sut.delete(storageFile);

        assertTrue(Files.exists(storagePath));
        assertTrue(result.getAsBoolean());
        assertFalse(Files.exists(storagePath));
    }

    @Test
    void givenNotExist_whenDelete_thenSuccess() throws IOException {
        FileResource testResource = new ClasspathFileResource(testFileName);
        StorageFile storageFile = sut.save(testResource);
        Path storagePath = sut.getStoragePath(storageFile.getFileName());
        Files.delete(storagePath);

        BooleanSupplier result = () -> sut.delete(storageFile);

        assertFalse(Files.exists(storagePath));
        assertTrue(result.getAsBoolean());
    }
}
