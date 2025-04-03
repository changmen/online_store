package com.example.onlinestore.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 */
@Getter
@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint:oss-cn-shenzhen.aliyuncs.com}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:TLAI5tMC62xyxXoT005Test}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:TLAI5tMC62xyxXoT005Test}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName:item_description}")
    private String bucketName;

}