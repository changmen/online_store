package com.example.onlinestore.service.impl;

import com.aliyun.oss.*;
import com.example.onlinestore.config.OssConfig;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.OssService;
import com.example.onlinestore.utils.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * 阿里云OSS服务实现类
 */
@Service
public class OssServiceImpl implements OssService {

    private static final Logger logger = LoggerFactory.getLogger(OssServiceImpl.class);
    private static final String ITEM_DESCRIPTION_PREFIX = "item/description";

    @Autowired
    private OssConfig ossConfig;

    private OSS ossClient;

    @PostConstruct
    private void init() {
        this.ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
    }

    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 上传商品描述文件到OSS
     *
     * @param content 商品描述内容
     * @return OSS文件URL
     */
    @Override
    public String uploadItemDescription(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content cannot be blank");
        }
        String objectName = generateItemDescriptionObjectName();

        try {
            // 将内容转换为输入流
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            try (InputStream inputStream = new ByteArrayInputStream(contentBytes)) {
                // 上传到OSS
                ossClient.putObject(ossConfig.getBucketName(), objectName, inputStream);

                // 返回OSS文件URL
                return generateOssUrl(objectName);
            }
        } catch (OSSException | ClientException | IOException e) {
            logger.error("Failed to upload item description to OSS", e);
            throw new BizException(ErrorCode.REQUEST_OSS_FAILED);
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
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ossUrl);

        try {
            // 执行HTTP请求
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.error("OSS 返回异常状态码: {}", statusCode);
                throw new BizException(ErrorCode.REQUEST_OSS_FAILED);
            }


            // 读取响应内容
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            logger.debug("Successfully retrieved item description from OSS URL: {}", ossUrl);
            return content.toString();
        } catch (IOException e) {
            logger.error("Failed to get item description from OSS URL: {}", ossUrl, e);
            throw new BizException(ErrorCode.REQUEST_OSS_FAILED);
        }
    }


    /**
     * 生成OSS对象名称
     */
    private String generateItemDescriptionObjectName() {
        return MessageFormat.format("{0}/{1}/{2}", ITEM_DESCRIPTION_PREFIX, DateUtils.getCurrentDate(), UUID.randomUUID());
    }

    /**
     * 生成OSS文件URL
     */
    private String generateOssUrl(String objectName) {
        return MessageFormat.format("https://{0}.{1}/{2}", ossConfig.getBucketName(), ossConfig.getEndpoint(), objectName);
    }


} 