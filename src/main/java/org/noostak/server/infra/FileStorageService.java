package org.noostak.server.infra;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String uploadImage(String directoryPath, MultipartFile file) throws IOException;
    String getImageUrl(String key);

    void deleteImage(String key) throws IOException;
}

