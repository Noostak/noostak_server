package org.noostak.server.infra;

import org.noostak.server.global.config.AwsConfig;

import software.amazon.awssdk.services.s3.S3Client;

import static org.mockito.Mockito.*;

class S3ServiceTest {

    private final AwsConfig awsConfig = mock(AwsConfig.class);
    private final S3Client s3Client = mock(S3Client.class);
    private final String bucketName = "test-bucket";
    private final S3Service s3Service = new S3Service(bucketName, awsConfig);
}
