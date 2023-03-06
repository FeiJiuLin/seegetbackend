package com.xmap.ar.config;

import io.minio.MinioClient;
import lombok.Data;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Data
public class MinioConfig {
//    @Value("${Minio.url}")
//    private String url;
//    @Value("${Minio.accessKey}")
//    private String accessKey;
//    @Value("${Minio.secretKey}")
//    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint("http://123.60.47.141:9000")
                .credentials("minioadmin", "cvrs213minio#@!").build();
    }


}