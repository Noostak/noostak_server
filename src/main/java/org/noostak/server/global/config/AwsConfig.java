package org.noostak.server.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
public class AwsConfig {

    @Value("${aws-property.s3-bucket-name}")
    private String s3BucketName;

    @Value("${aws-property.access-key}")
    private String accessKey;

    @Value("${aws-property.secret-key}")
    private String secretKey;

    @Value("${aws-property.aws-region}")
    private String awsRegion;

    @Value("${aws-property.max-file-size}")
    private String maxfileSize;

    @Bean
    public Region getRegion() {

        return Region.of(awsRegion);
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}