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
 */
@Component
@Endpoint(id = "systeminfo")
public class SystemInfoEndpoint {

    @ReadOperation
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        Properties sysProps = System.getProperties();
        systemInfo.put("systemProperties", sysProps);
        
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        systemInfo.put("jvmArguments", runtimeBean.getInputArguments());
        
        Map<String, Object> fileSystemInfo = new HashMap<>();
        File[] roots = File.listRoots();
        for (File root : roots) {
            Map<String, Object> driveInfo = new HashMap<>();
            driveInfo.put("totalSpace", root.getTotalSpace());
            driveInfo.put("freeSpace", root.getFreeSpace());
            driveInfo.put("usableSpace", root.getUsableSpace());
            
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
        
        Map<String, String> envVars = System.getenv();
        systemInfo.put("environmentVariables", envVars);
        
        systemInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        systemInfo.put("freeMemory", Runtime.getRuntime().freeMemory());
        systemInfo.put("maxMemory", Runtime.getRuntime().maxMemory());
        systemInfo.put("totalMemory", Runtime.getRuntime().totalMemory());
        
        return systemInfo;
    }
} 