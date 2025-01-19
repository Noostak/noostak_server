package org.noostak.server.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ImageHandlerServiceTest {

    private ImageHandlerService imageHandlerService;
    private MockFileStorageService mockFileStorageService;

    @BeforeEach
    void setUp() {
        mockFileStorageService = new MockFileStorageService();
        imageHandlerService = new ImageHandlerService(mockFileStorageService);
    }

    @Test
    void testUploadImage() throws Exception {
        // given
        String directoryPath = "/profile-images/";
        MultipartFile mockImage = mock(MultipartFile.class);

        // when
        String actualUrl = imageHandlerService.uploadImage(directoryPath, mockImage);

        // then
        assertEquals("https://mock-url.com/test-image.png", actualUrl);
    }

    @Test
    void testGetImageUrl() {

        // given
        String key = "test-key";

        // when
        String actualUrl = imageHandlerService.getImageUrl(key);

        // then
        assertEquals("http://mocked-url/" + key, actualUrl);
    }

    @Test
    void testDeleteImage() throws Exception {

        // given
        String key = "test-key";

        // when
        imageHandlerService.deleteImage(key);

    }
}
