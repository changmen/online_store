package com.example.onlinestore.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.example.onlinestore.config.OssConfig;
import com.example.onlinestore.service.OssService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(OssServiceImpl.class);
    private static final String ITEM_DESCRIPTION_PREFIX = "item/description/";
    private static final String ITEM_DESCRIPTION_SUFFIX = ".html";

    private final OSS ossClient;
    private final OssConfig ossConfig;
    private final CloseableHttpClient httpClient;

    public OssServiceImpl(OSS ossClient, OssConfig ossConfig) {
        this.ossClient = ossClient;
        this.ossConfig = ossConfig;
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public void destroy() throws Exception {
        httpClient.close();
    }

    /**
     * 上传商品描述文件到OSS（安全实现）
     * 
     * @param itemId 商品ID
     * @param content 商品描述内容
     * @return OSS文件URL
     */
    @Override
    public String uploadItemDescription(Long itemId, String content) {
        String objectName = generateObjectName(itemId);

        try {
            // 将内容转换为输入流
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            try (InputStream inputStream = new ByteArrayInputStream(contentBytes)) {
                // 上传到OSS
                ossClient.putObject(ossConfig.getBucketName(), objectName, inputStream);
                logger.info("Successfully uploaded item description for item: {}", itemId);

                // 返回OSS文件URL
                return generateOssUrl(objectName);
            }
        } catch (OSSException | ClientException | IOException e) {
            logger.error("Failed to upload item description to OSS for item: {}", itemId, e);
            throw new RuntimeException("Failed to upload item description to OSS", e);
        }
    }

    /**
     * 从OSS获取商品描述内容（安全实现）
     * 
     * @param ossUrl OSS文件URL
     * @return 商品描述内容
     */
    @Override
    public String getItemDescription(String ossUrl) {
        HttpGet httpGet = new HttpGet(ossUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            try (InputStream inputStream = response.getEntity().getContent();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                logger.info("Successfully retrieved item description from OSS URL: {}", ossUrl);
                return content.toString();
            }
        } catch (IOException e) {
            logger.error("Failed to get item description from OSS URL: {}", ossUrl, e);
            throw new RuntimeException("Failed to get item description from OSS", e);
        }
    }


    /**
     * 生成OSS对象名称
     */
    private String generateObjectName(Long itemId) {
        return ITEM_DESCRIPTION_PREFIX + itemId + "_" + UUID.randomUUID().toString() + ITEM_DESCRIPTION_SUFFIX;
    }

    /**
     * 生成OSS文件URL
     */
    private String generateOssUrl(String objectName) {
        return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + objectName;
    }
} 