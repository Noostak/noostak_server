package org.noostak.server.infra;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String uploadImage(String directoryPath, MultipartFile file) throws IOException;

    void deleteImage(String key) throws IOException;
}

