package org.noostak.server.infra;

import lombok.RequiredArgsConstructor;
import org.noostak.server.global.config.AwsConfig;
import org.noostak.server.infra.error.S3ErrorCode;
import org.noostak.server.infra.error.S3Exception;
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

        return key;
    }

    public String generateFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                awsConfig.getS3BucketName(),
                awsConfig.getRegion().id(),
                key);
    }

    @Override
    public void deleteImage(String key) throws IOException {
        try {
            S3Client s3Client = awsConfig.getS3Client();

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(awsConfig.getS3BucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new S3Exception(S3ErrorCode.OBJECT_NOT_FOUND);
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }

    private void validateFile(MultipartFile file) {
        if (!IMAGE_EXTENSIONS.contains(file.getContentType())) {
            throw new S3Exception(S3ErrorCode.INVALID_EXTENSION);
        }

        if (file.getSize() > awsConfig.getMaxFileSize()) {
            throw new S3Exception(S3ErrorCode.FILE_SIZE_EXCEEDED);
        }
    }
}
