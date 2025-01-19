package org.noostak.server.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.global.config.AwsConfig;
import org.noostak.server.infra.error.S3ErrorCode;
import org.noostak.server.infra.error.S3Exception;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private final AwsConfig awsConfig = mock(AwsConfig.class);
    private final S3Client s3Client = mock(S3Client.class);

    private final S3Service s3Service = new S3Service(awsConfig);
    private final String bucketName = "test-bucket";

    @Nested
    @DisplayName("파일 업로드 테스트")
    class UploadImageTests {

        @Test
        @DisplayName("이미지 업로드 - 성공")
        void uploadImage_success() throws IOException {
            // Given
            MultipartFile file = createMockImage("profile.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});
            String directoryPath = "profile/";

            // Mocking AWS S3 interactions
            setupMockForUpload();

            // When
            String key = s3Service.uploadImage(directoryPath, file);

            // Then
            assertThat(key).startsWith(directoryPath)
                    .endsWith(".jpg");

            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        @DisplayName("이미지 업로드 - 잘못된 확장자")
        void uploadImage_invalidExtension() {
            // Given
            MultipartFile file = createMockImage("invalid.txt", "text/plain", new byte[]{1, 2, 3, 4});

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadImage("profile/", file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.INVALID_EXTENSION.getMessage());
        }

        @Test
        @DisplayName("이미지 업로드 - 파일 크기 초과")
        void uploadImage_fileSizeExceedsLimit() {
            // Given
            MultipartFile file = createMockImage("large.jpg", "image/jpeg", new byte[3 * 1024 * 1024]);
            when(awsConfig.getMaxFileSize()).thenReturn(2L * 1024 * 1024); // Set max file size to 2MB

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadImage("profile/", file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.FILE_SIZE_EXCEEDED.getMessage());
        }
    }

    @Nested
    @DisplayName("URL 생성 테스트")
    class GenerateFileUrlTests {

        @Test
        @DisplayName("URL 생성 - 성공")
        void generateFileUrl_success() {
            // Given
            setupMockForUpload();
            String key = "profile/abc123.jpg";

            // When
            String fileUrl = s3Service.generateFileUrl(key);

            // Then
            assertThat(fileUrl).startsWith("https://")
                    .contains("test-bucket")
                    .contains("profile")
                    .endsWith(".jpg");
        }
    }

    @Nested
    @DisplayName("이미지 삭제 테스트")
    class DeleteImageTests {

        @Test
        @DisplayName("이미지 삭제 - 성공")
        void deleteImage_success() throws IOException {
            // Given
            String key = "images/test.jpg";
            when(awsConfig.getS3Client()).thenReturn(s3Client);
            when(awsConfig.getS3BucketName()).thenReturn(bucketName);

            // When
            s3Service.deleteImage(key);

            // Then
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            verify(s3Client).deleteObject(eq(deleteRequest));
        }

        @Test
        @DisplayName("이미지 삭제 - 키가 없는 경우 예외 발생")
        void deleteImage_keyNotFound_throwsException() {
            // Given
            String invalidKey = "invalid/path/image.jpg";

            when(awsConfig.getS3Client()).thenReturn(s3Client);
            when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                    .thenThrow(new S3Exception(S3ErrorCode.OBJECT_NOT_FOUND));

            // When & Then
            assertThatThrownBy(() -> s3Service.deleteImage(invalidKey))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.OBJECT_NOT_FOUND.getMessage());
        }
    }

    private MultipartFile createMockImage(String fileName, String contentType, byte[] content) {
        return new MockMultipartFile("image", fileName, contentType, content);
    }

    private void setupMockForUpload() {
        when(awsConfig.getS3Client()).thenReturn(s3Client);
        when(awsConfig.getS3BucketName()).thenReturn(bucketName);
        when(awsConfig.getMaxFileSize()).thenReturn(1024L * 1024 * 2);
        when(awsConfig.getRegion()).thenReturn(software.amazon.awssdk.regions.Region.of("ap-northeast-2"));
    }
}