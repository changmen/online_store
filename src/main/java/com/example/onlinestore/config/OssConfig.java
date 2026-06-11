package com.example.onlinestore.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 */
@Getter
@Configuration
@RefreshScope
public class OssConfig {

    @Value("${aliyun.oss.endpoint:oss-cn-shenzhen.aliyuncs.com}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName:item_description}")
    private String bucketName;

    @PostConstruct
    public void validate() {
        if (StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret)) {
            throw new IllegalStateException("aliyun.oss.accessKeyId and aliyun.oss.accessKeySecret must be configured");
        }
    }

}