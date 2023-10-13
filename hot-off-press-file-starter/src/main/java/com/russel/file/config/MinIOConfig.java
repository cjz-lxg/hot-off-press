package com.russel.file.config;

import com.russel.file.service.FileStorageService;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({MinIOConfigProperties.class})
@ConditionalOnClass(FileStorageService.class)
public class MinIOConfig {

    @Autowired
    private final MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient buildMinioClient() {
        return MinioClient.builder()
                .endpoint(minIOConfigProperties.getEndpoint())
                .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
                .build();
    }
}
