package com.example.onlinestore.controller;

import com.example.onlinestore.dto.CategoryItemCountDTO;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 商品统计控制器
 */
@RestController
@RequestMapping("/api/v1/items/statistics")
public class ItemStatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemStatisticsController.class);
    
    private final ItemService itemService;

    public ItemStatisticsController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    /**
     * 按类目统计商品数量
     * 
     * @return 类目商品数量统计列表
     */
    @GetMapping("/by-category")
    public Response<List<CategoryItemCountDTO>> countItemsByCategory() {
        logger.info("API - Count items by category");
        List<CategoryItemCountDTO> result = itemService.countItemsByCategory();
        return Response.success(result);
    }
    
    /**
     * 按类目统计商品数量（包含子类目）
     * 
     * @param includeSubcategories 是否包含子类目的商品
     * @return 类目ID到商品数量的映射
     */
    @GetMapping("/by-category-with-subcategories")
    public Response<Map<Long, Long>> countItemsByCategoryWithSubcategories(
            @RequestParam(value = "includeSubcategories", defaultValue = "false") boolean includeSubcategories) {
        logger.info("API - Count items by category with subcategories, includeSubcategories={}", includeSubcategories);
        Map<Long, Long> result = itemService.countItemsByCategoryWithSubcategories(includeSubcategories);
        return Response.success(result);
    }
} 