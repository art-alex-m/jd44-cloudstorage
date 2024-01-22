package ru.netology.cloudstorage.contracts.storage.model;

import java.io.IOException;
import java.io.InputStream;

public interface FileResource extends FileInfo {
    InputStream getInputStream() throws IOException;
}
