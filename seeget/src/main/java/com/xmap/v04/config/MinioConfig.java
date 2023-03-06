package com.xmap.v04.config;
import io.minio.MinioClient;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Configuration
@Data
public class MinioConfig {

    @Value("${Minio.url}")
    private String url;
    @Value("${Minio.accessKey}")
    private String accessKey;
    @Value("${Minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {//创建minio初始化对象
        return MinioClient.builder().endpoint(url)
                .credentials(accessKey, secretKey).build();
    }

}