package org.noostak.server.infra;

import lombok.RequiredArgsConstructor;
import org.noostak.server.global.config.AwsConfig;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.infra.error.S3UploadErrorCode;
import org.noostak.server.infra.error.S3UploadException;
import org.noostak.server.member.domain.vo.ProfileImageUrl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Service implements FileStorageService {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

    private final AwsConfig awsConfig;

    @Override
    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        validateFile(image);

        String key = directoryPath + generateFileName(image.getOriginalFilename());
        S3Client s3Client = awsConfig.getS3Client();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsConfig.getS3BucketName())
                .key(key)
                .contentType(image.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(image.getBytes()));

        return generateFileUrl(key); // S3 URL 반환
    }

    @Override
    public void deleteImage(String key) throws IOException {
        S3Client s3Client = awsConfig.getS3Client();

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(awsConfig.getS3BucketName())
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    public ProfileImageUrl uploadProfileImage(MultipartFile file) throws IOException {
        String url = uploadImage("/profile/", file); // /profile 디렉토리에 저장
        return ProfileImageUrl.from(url);
    }

    public GroupImageUrl uploadGroupImage(MultipartFile file) throws IOException {
        String url = uploadImage("/group/", file); // /group 디렉토리에 저장
        return GroupImageUrl.from(url);
    }

    private String generateFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }

    private String generateFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                awsConfig.getS3BucketName(),
                awsConfig.getRegion().id(),
                key);
    }

    private void validateFile(MultipartFile file) {
        if (!IMAGE_EXTENSIONS.contains(file.getContentType())) {
            throw new S3UploadException(S3UploadErrorCode.INVALID_EXTENSION);
        }

        if (file.getSize() > awsConfig.getMaxFileSize()) {
            throw new S3UploadException(S3UploadErrorCode.FILE_SIZE_EXCEEDED);
        }
    }
}
