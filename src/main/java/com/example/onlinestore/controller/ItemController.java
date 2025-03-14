package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.exception.ItemNameInvalidException;
import com.example.onlinestore.service.CategoryService;
import com.example.onlinestore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * 创建商品
     */
    @PostMapping
    public Response<Long> createItem(@RequestBody Item item, @RequestHeader("X-User-Id") String userId) {
        try {
            itemService.addItem(userId, item);
            return Response.success(item.getId());
        } catch (ItemNameInvalidException e) {
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public Response<Void> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
        try {
            item.setId(id);
            itemService.updateItem(item);
            return Response.success();
        } catch (ItemNameInvalidException e) {
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Response<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
        return Response.success();
    }

    /**
     * 商品列表查询（支持按类目ID精确搜索和商品名称模糊搜索）
     */
    @GetMapping
    public Response<PageResponse<ItemDetailDTO>> listItems(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        // 构建查询条件
        ItemQueryDTO queryDTO = new ItemQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setName(name);
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        
        // 查询商品列表
        List<Item> items = itemService.queryItems(queryDTO);
        
        // 查询总数
        long total = itemService.countItems(queryDTO);
        
        // 转换为详情DTO
        List<ItemDetailDTO> detailDTOs = items.stream().map(item -> {
            ItemDetailDTO dto = new ItemDetailDTO();
            dto.setItem(item);
            
            // 获取SKU列表
            List<Sku> skus = itemService.getSkusByItemId(item.getId());
            dto.setSkus(skus);
            
            // 获取类目信息
            if (item.getCategoryId() != null) {
                Category category = categoryService.getCategoryById(item.getCategoryId());
                if (category != null) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setId(category.getId());
                    categoryDTO.setName(category.getName());
                    dto.setCategory(categoryDTO);
                }
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        // 构建分页响应
        PageResponse<ItemDetailDTO> pageResponse = PageResponse.of(detailDTOs, total, page, size);
        
        return Response.success(pageResponse);
    }

    /**
     * 为商品添加SKU
     */
    @PostMapping("/{itemId}/skus")
    public Response<Long> addSkuToItem(@PathVariable("itemId") Long itemId, @RequestBody Sku sku) {
        itemService.addSkuToItem(itemId, sku);
        return Response.success(sku.getId());
    }

    /**
     * 获取商品的所有SKU
     */
    @GetMapping("/{itemId}/skus")
    public Response<List<Sku>> getItemSkus(@PathVariable("itemId") Long itemId) {
        List<Sku> skus = itemService.getSkusByItemId(itemId);
        return Response.success(skus);
    }

    /**
     * 更新商品的SKU
     */
    @PutMapping("/{itemId}/skus/{skuId}")
    public Response<Void> updateItemSku(@PathVariable("itemId") Long itemId, 
                                          @PathVariable("skuId") Long skuId, 
                                          @RequestBody Sku sku) {
        sku.setId(skuId);
        sku.setItemId(itemId);
        itemService.updateItemSku(sku);
        return Response.success();
    }

    /**
     * 删除商品的SKU
     */
    @DeleteMapping("/{itemId}/skus/{skuId}")
    public Response<Void> deleteItemSku(@PathVariable("itemId") Long itemId, 
                                          @PathVariable("skuId") Long skuId) {
        itemService.deleteItemSku(skuId);
        return Response.success();
    }
}
