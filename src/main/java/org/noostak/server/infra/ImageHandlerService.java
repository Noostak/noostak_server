package org.noostak.server.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageHandlerService {

    private final FileStorageService fileStorageService;

    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        return fileStorageService.uploadImage(directoryPath, image);
    }

    public void deleteImage(String key) throws IOException {
        fileStorageService.deleteImage(key);
    }

    public String getImageUrl(String key) {
        return fileStorageService.getImageUrl(key);
    }
}
