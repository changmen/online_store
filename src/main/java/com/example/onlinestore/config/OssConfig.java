package com.example.onlinestore.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 */
@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint:test}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:test}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:test}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName:test}")
    private String bucketName;

    /**
     * 创建OSS客户端
     */
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }
} 