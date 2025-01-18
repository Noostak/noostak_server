package org.noostak.server.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.global.config.AwsConfig;
import org.noostak.server.infra.error.S3ErrorCode;
import org.noostak.server.infra.error.S3Exception;
import org.noostak.server.member.domain.vo.ProfileImageUrl;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
    @DisplayName("프로필 이미지 업로드 테스트")
    class UploadProfileImageTests {
        private final String profileDirectory = "profile/";

        @Test
        @DisplayName("이미지 업로드 - 성공")
        void uploadProfileImage_success() throws IOException {
            // Given
            MultipartFile file = createMockImage("profile.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});
            setupMockForUpload();

            // When
            ProfileImageUrl result = s3Service.uploadProfileImage(file);

            // Then
            assertThat(result.toString())
                    .startsWith("https://test-bucket.s3.")
                    .contains(profileDirectory)
                    .endsWith(".jpg");
            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        @DisplayName("이미지 업로드 - 잘못된 확장자")
        void uploadProfileImage_invalidExtension() {
            // Given
            MultipartFile file = createMockImage("invalid.txt", "text/plain", new byte[]{1, 2, 3, 4});

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadProfileImage(file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.INVALID_EXTENSION.getMessage());
        }

        @Test
        @DisplayName("이미지 업로드 - 파일 크기 초과")
        void uploadProfileImage_fileSizeExceedsLimit() {
            // Given
            MultipartFile file = createMockImage("large.jpg", "image/jpeg", new byte[3 * 1024 * 1024]);
            when(awsConfig.getMaxFileSize()).thenReturn(2L * 1024 * 1024);

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadProfileImage(file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.FILE_SIZE_EXCEEDED.getMessage());
        }
    }

    @Nested
    @DisplayName("그룹 이미지 업로드 테스트")
    class UploadGroupImageTests {
        private final String groupDirectory = "group/";

        @Test
        @DisplayName("이미지 업로드 - 성공")
        void uploadGroupImage_success() throws IOException {
            // Given
            MultipartFile file = createMockImage("group.png", "image/png", new byte[]{1, 2, 3, 4});
            setupMockForUpload();

            // When
            GroupImageUrl result = s3Service.uploadGroupImage(file);

            // Then
            assertThat(result.toString())
                    .startsWith("https://test-bucket.s3.")
                    .contains(groupDirectory)
                    .endsWith(".png");
            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        @DisplayName("이미지 업로드 - 잘못된 확장자")
        void uploadGroupImage_invalidExtension() {
            // Given
            MultipartFile file = createMockImage("invalid.txt", "text/plain", new byte[]{1, 2, 3, 4});

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadGroupImage(file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.INVALID_EXTENSION.getMessage());
        }

        @Test
        @DisplayName("이미지 업로드 - 파일 크기 초과")
        void uploadGroupImage_fileSizeExceedsLimit() {
            // Given
            MultipartFile file = createMockImage("large.jpg", "image/jpeg", new byte[3 * 1024 * 1024]);
            when(awsConfig.getMaxFileSize()).thenReturn(2L * 1024 * 1024);

            // When & Then
            assertThatThrownBy(() -> s3Service.uploadGroupImage(file))
                    .isInstanceOf(S3Exception.class)
                    .hasMessageContaining(S3ErrorCode.FILE_SIZE_EXCEEDED.getMessage());
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