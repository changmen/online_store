package com.example.onlinestore.service;

/**
 * 阿里云OSS服务接口
 */
public interface OssService {
    
    /**
     * 上传商品描述文件到OSS
     * 
     * @param itemId 商品ID
     * @param content 商品描述内容
     * @return OSS文件URL
     */
    String uploadItemDescription(String content);
    
    /**
     * 从OSS获取商品描述内容
     * 
     * @param ossUrl OSS文件URL
     * @return 商品描述内容
     */
    String getItemDescription(String ossUrl);

} 