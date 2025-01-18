package org.noostak.server.infra;

import org.springframework.web.multipart.MultipartFile;

public class MockFileStorageService implements FileStorageService {

    @Override
    public String uploadImage(String directoryPath, MultipartFile file) {
        return "https://mock-url.com/test-image.png";
    }

    @Override
    public void deleteImage(String key) {
    }
}

