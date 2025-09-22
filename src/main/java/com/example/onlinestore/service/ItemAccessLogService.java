package com.example.onlinestore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品访问日志服务接口
 */
public interface ItemAccessLogService {

    /**
     * 记录商品访问日志
     *
     * @param itemId    商品ID
     * @param memberId    用户ID
     * @param ip        访问IP
     * @param userAgent 用户代理
     * @param referer   来源页面
     */
    void recordAccess(Long itemId, String itemName, String memberId, String userName, String ip, String userAgent, String referer, String sessionId);

    /**
     * 异步记录商品访问日志
     *
     * @param itemId    商品ID
     * @param memberId    用户ID
     * @param ip        访问IP
     * @param userAgent 用户代理
     * @param referer   来源页面
     */
    void asyncRecordAccessLog(Long itemId, String itemName, String memberId, String userName, String ip, String userAgent, String referer, String sessionId);

    /**
     * 获取商品在指定时间范围内的访问次数
     *
     * @param itemId    商品ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 访问次数
     */
    int getAccessCount(Long itemId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取热门商品列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param limit     限制数量
     * @return 热门商品列表
     */
    List<Map<String, Object>> getHotItems(LocalDateTime startTime, LocalDateTime endTime, int limit);

} 