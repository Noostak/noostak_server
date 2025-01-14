package org.noostak.server.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.global.config.AwsConfig;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private final AwsConfig awsConfig = mock(AwsConfig.class);
    private final S3Client s3Client = mock(S3Client.class);
    private final String bucketName = "test-bucket";
    private final S3Service s3Service = new S3Service(bucketName, awsConfig);

    @Nested
    @DisplayName("Image Upload Tests")
    class UploadImageTests {

        @Test
        @DisplayName("이미지 업로드 - 성공")
        void uploadImage_success() throws IOException {

            // Given
            String directoryPath = "images/";
            MultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    new byte[]{1, 2, 3, 4}
            );
            when(awsConfig.getS3Client()).thenReturn(s3Client);

            // When
            String result = s3Service.uploadImage(directoryPath, image);

            // Then
            assertThat(result).startsWith(directoryPath).endsWith(".jpg");
            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }
    }

    @Test
    @DisplayName("이미지 업로드 - 잘못된 확장자")
    void uploadImage_invalidExtension() {
        // Given
        String directoryPath = "images/";
        MultipartFile image = new MockMultipartFile(
                "image",
                "test.txt",
                "text/plain",
                new byte[]{1, 2, 3, 4}
        );

        // When & Then
        assertThatThrownBy(() -> s3Service.uploadImage(directoryPath, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미지 확장자는 jpg, png, webp만 가능합니다.");
    }

    @Test
    @DisplayName("이미지 업로드 - 파일 크기 초과")
    void uploadImage_fileSizeExceedsLimit() {
        // Given
        String directoryPath = "images/";
        MultipartFile image = new MockMultipartFile(
                "image",
                "large.jpg",
                "image/jpeg",
                new byte[6 * 1024 * 1024]
        );

        // When & Then
        assertThatThrownBy(() -> s3Service.uploadImage(directoryPath, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미지 사이즈는 5MB를 넘을 수 없습니다.");
    }


}
