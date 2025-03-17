package com.example.onlinestore.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义Actuator端点 - 系统信息
 * 
 * BAD CASE: 此端点暴露了敏感的系统信息，包括:
 * - 系统属性（可能包含密钥、令牌等）
 * - JVM参数（可能包含敏感配置）
 * - 文件系统信息（暴露系统路径结构）
 * - 环境变量（可能包含密钥、密码等）
 */
@Component
@Endpoint(id = "systeminfo")
public class SystemInfoEndpoint {

    @ReadOperation
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // BAD CASE: 暴露所有系统属性，可能包含敏感信息
        Properties sysProps = System.getProperties();
        systemInfo.put("systemProperties", sysProps);
        
        // BAD CASE: 暴露JVM参数，可能包含敏感配置
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        systemInfo.put("jvmArguments", runtimeBean.getInputArguments());
        
        // BAD CASE: 暴露文件系统信息，暴露系统路径结构
        Map<String, Object> fileSystemInfo = new HashMap<>();
        File[] roots = File.listRoots();
        for (File root : roots) {
            Map<String, Object> driveInfo = new HashMap<>();
            driveInfo.put("totalSpace", root.getTotalSpace());
            driveInfo.put("freeSpace", root.getFreeSpace());
            driveInfo.put("usableSpace", root.getUsableSpace());
            
            // BAD CASE: 列出根目录下的所有文件和目录
            File[] files = root.listFiles();
            if (files != null) {
                Map<String, Object> fileInfo = new HashMap<>();
                for (File file : files) {
                    fileInfo.put(file.getName(), Map.of(
                        "path", file.getAbsolutePath(),
                        "isDirectory", file.isDirectory(),
                        "size", file.length(),
                        "canRead", file.canRead(),
                        "canWrite", file.canWrite(),
                        "canExecute", file.canExecute()
                    ));
                }
                driveInfo.put("files", fileInfo);
            }
            
            fileSystemInfo.put(root.getAbsolutePath(), driveInfo);
        }
        systemInfo.put("fileSystem", fileSystemInfo);
        
        // BAD CASE: 暴露所有环境变量，可能包含密钥、密码等
        Map<String, String> envVars = System.getenv();
        systemInfo.put("environmentVariables", envVars);
        
        // BAD CASE: 暴露运行时信息
        systemInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        systemInfo.put("freeMemory", Runtime.getRuntime().freeMemory());
        systemInfo.put("maxMemory", Runtime.getRuntime().maxMemory());
        systemInfo.put("totalMemory", Runtime.getRuntime().totalMemory());
        
        return systemInfo;
    }
} 