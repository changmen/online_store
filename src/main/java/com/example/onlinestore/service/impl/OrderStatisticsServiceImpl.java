package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.OrderStatistics;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import com.example.onlinestore.entity.OrderStatisticsEntity;
import com.example.onlinestore.mapper.OrderItemMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.mapper.OrderStatisticsMapper;
import com.example.onlinestore.service.OrderStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OrderStatisticsServiceImpl implements OrderStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(OrderStatisticsServiceImpl.class);
    private static final int BATCH_SIZE = 1000;

    private final Map<Long, Map<Long, OrderStatisticsEntity>> userOrderStatisticsMap = new ConcurrentHashMap<>();

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    @Transactional
    public void generateStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("开始生成订单统计: startDate={}, endDate={}", startDate, endDate);
        
        // 获取订单列表
        List<OrderEntity> orders = orderMapper.findByCondition(null, null, startDate, endDate, 0, Integer.MAX_VALUE);
        if (orders.isEmpty()) {
            logger.info("没有找到需要统计的订单");
            return;
        }


        // 处理每个订单
        for (OrderEntity order : orders) {
            // 获取订单项
            List<OrderItemEntity> orderItems = orderItemMapper.findByOrderId(order.getId());
            
            // 按商品统计
            for (OrderItemEntity orderItem : orderItems) {
                // 用户维度统计
                userOrderStatisticsMap.computeIfAbsent(order.getUserId(), k -> new HashMap<>())
                    .computeIfAbsent(orderItem.getItemId(), k -> {
                        OrderStatisticsEntity stats = new OrderStatisticsEntity();
                        stats.setUserId(order.getUserId());
                        stats.setItemId(orderItem.getItemId());
                        stats.setStatisticsDate(order.getCreatedAt().toLocalDate().atStartOfDay());
                        stats.setOrderCount(0);
                        stats.setTotalAmount(BigDecimal.ZERO);
                        return stats;
                    });
                
                // 更新统计数据
                OrderStatisticsEntity stats = userOrderStatisticsMap.get(order.getUserId()).get(orderItem.getItemId());
                stats.setOrderCount(stats.getOrderCount() + 1);
                stats.setTotalAmount(stats.getTotalAmount().add(orderItem.getTotalAmount()));
            }
        }

        // 批量保存统计数据
        List<OrderStatisticsEntity> statisticsList = new ArrayList<>();
        for (Map<Long, OrderStatisticsEntity> itemStats : userOrderStatisticsMap.values()) {
            statisticsList.addAll(itemStats.values());
        }

        // 分批保存，避免内存溢出
        for (int i = 0; i < statisticsList.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, statisticsList.size());
            List<OrderStatisticsEntity> batch = statisticsList.subList(i, end);
            orderStatisticsMapper.batchInsertStatistics(batch);
        }

        
        logger.info("订单统计生成完成");
    }

    @Override
    public List<OrderStatistics> getUserStatistics(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderStatisticsEntity> entities = orderStatisticsMapper.findByUserId(userId, startDate, endDate);
        return entities.stream()
            .map(this::convertToBean)
            .collect(Collectors.toList());
    }

    @Override
    public List<OrderStatistics> getItemStatistics(Long itemId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderStatisticsEntity> entities = orderStatisticsMapper.findByItemId(itemId, startDate, endDate);
        return entities.stream()
            .map(this::convertToBean)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cleanHistoryStatistics(LocalDateTime beforeDate) {
        orderStatisticsMapper.deleteBeforeDate(beforeDate);
    }

    @Override
    public Map<String, Object> getUserStatisticsSummary(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderStatistics> statistics = getUserStatistics(userId, startDate, endDate);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", statistics.stream().mapToInt(OrderStatistics::getOrderCount).sum());
        summary.put("totalAmount", statistics.stream()
            .map(OrderStatistics::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.put("uniqueItems", statistics.size());
        
        return summary;
    }

    @Override
    public Map<String, Object> getItemStatisticsSummary(Long itemId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderStatistics> statistics = getItemStatistics(itemId, startDate, endDate);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", statistics.stream().mapToInt(OrderStatistics::getOrderCount).sum());
        summary.put("totalAmount", statistics.stream()
            .map(OrderStatistics::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.put("uniqueUsers", statistics.size());
        
        return summary;
    }

    /**
     * 将实体对象转换为Bean对象
     */
    private OrderStatistics convertToBean(OrderStatisticsEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderStatistics bean = new OrderStatistics();
        bean.setId(entity.getId());
        bean.setUserId(entity.getUserId());
        bean.setItemId(entity.getItemId());
        bean.setOrderCount(entity.getOrderCount());
        bean.setTotalAmount(entity.getTotalAmount());
        bean.setStatisticsDate(entity.getStatisticsDate());
        bean.setCreatedAt(entity.getCreatedAt());
        bean.setUpdatedAt(entity.getUpdatedAt());
        return bean;
    }
}