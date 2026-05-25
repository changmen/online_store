package com.example.onlinestore.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

@Component
@Endpoint(id = "configinfo")
public class ConfigInfoEndpoint {

    private static final String MASKED = "***";
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "spring.datasource.password",
            "spring.data.redis.password",
            "admin.auth.password",
            "cloud.aws.credentials.secretKey",
            "aliyun.oss.accessKeySecret"
    );

    private final ConfigurableEnvironment environment;

    public ConfigInfoEndpoint(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @ReadOperation
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> configInfo = new HashMap<>();

        configInfo.put("activeProfiles", environment.getActiveProfiles());

        Map<String, Object> nonSensitiveConfig = new HashMap<>();
        nonSensitiveConfig.put("spring.datasource.url", environment.getProperty("spring.datasource.url"));
        nonSensitiveConfig.put("spring.datasource.username", environment.getProperty("spring.datasource.username"));
        nonSensitiveConfig.put("spring.data.redis.host", environment.getProperty("spring.data.redis.host"));
        nonSensitiveConfig.put("spring.data.redis.port", environment.getProperty("spring.data.redis.port"));
        nonSensitiveConfig.put("admin.auth.username", environment.getProperty("admin.auth.username"));
        configInfo.put("config", nonSensitiveConfig);

        Map<String, Object> maskedSensitive = new HashMap<>();
        for (String key : SENSITIVE_KEYS) {
            String value = environment.getProperty(key);
            maskedSensitive.put(key, value != null ? MASKED : null);
        }
        configInfo.put("sensitiveConfig", maskedSensitive);

        return configInfo;
    }
} 