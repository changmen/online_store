package com.example.onlinestore.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * 自定义Actuator端点 - 配置信息
 * 
 * BAD CASE: 此端点暴露了敏感的配置信息，包括:
 * - 所有环境变量和系统属性
 * - 应用程序配置（可能包含密码、密钥等）
 * - 云服务配置（可能包含访问凭证）
 */
@Component
@Endpoint(id = "configinfo")
public class ConfigInfoEndpoint {

    @Autowired
    private ConfigurableEnvironment environment;

    @ReadOperation
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> configInfo = new HashMap<>();
        
        // BAD CASE: 暴露所有属性源
        Map<String, Map<String, Object>> propertySources = new HashMap<>();
        
        StreamSupport.stream(environment.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .forEach(ps -> {
                    EnumerablePropertySource<?> enumPS = (EnumerablePropertySource<?>) ps;
                    Map<String, Object> properties = new HashMap<>();
                    
                    // BAD CASE: 暴露所有属性，包括敏感信息
                    Arrays.stream(enumPS.getPropertyNames()).forEach(propName -> {
                        properties.put(propName, environment.getProperty(propName));
                    });
                    
                    propertySources.put(ps.getName(), properties);
                });
        
        configInfo.put("propertySources", propertySources);
        
        // BAD CASE: 特别暴露一些敏感配置
        Map<String, Object> sensitiveConfig = new HashMap<>();
        
        // 数据库配置
        sensitiveConfig.put("spring.datasource.url", environment.getProperty("spring.datasource.url"));
        sensitiveConfig.put("spring.datasource.username", environment.getProperty("spring.datasource.username"));
        sensitiveConfig.put("spring.datasource.password", environment.getProperty("spring.datasource.password"));
        
        // Redis配置
        sensitiveConfig.put("spring.data.redis.host", environment.getProperty("spring.data.redis.host"));
        sensitiveConfig.put("spring.data.redis.port", environment.getProperty("spring.data.redis.port"));
        sensitiveConfig.put("spring.data.redis.password", environment.getProperty("spring.data.redis.password"));
        
        // 管理员凭证
        sensitiveConfig.put("admin.auth.username", environment.getProperty("admin.auth.username"));
        sensitiveConfig.put("admin.auth.password", environment.getProperty("admin.auth.password"));
        
        // 云服务配置
        sensitiveConfig.put("cloud.aws.credentials.accessKey", environment.getProperty("cloud.aws.credentials.accessKey"));
        sensitiveConfig.put("cloud.aws.credentials.secretKey", environment.getProperty("cloud.aws.credentials.secretKey"));
        
        configInfo.put("sensitiveConfig", sensitiveConfig);
        
        // BAD CASE: 暴露活动的配置文件
        configInfo.put("activeProfiles", environment.getActiveProfiles());
        
        // BAD CASE: 暴露所有属性
        Map<String, Object> allProperties = new HashMap<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> enumerablePropertySource = (EnumerablePropertySource<?>) propertySource;
                for (String propertyName : enumerablePropertySource.getPropertyNames()) {
                    allProperties.put(propertyName, environment.getProperty(propertyName));
                }
            }
        }
        configInfo.put("allProperties", allProperties);
        
        return configInfo;
    }
} 