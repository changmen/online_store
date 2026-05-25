package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.service.CategoryService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final SkuService skuService;

    public ItemController(ItemService itemService, CategoryService categoryService, SkuService skuService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.skuService = skuService;
    }

    @PostMapping
    public Response<Long> createItem(@RequestBody Item item, @RequestHeader("X-User-Id") String userId) {
        itemService.addItem(userId, item);
        return Response.success(item.getId());
    }

    @PutMapping("/{id}")
    public Response<Void> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
        item.setId(id);
        itemService.updateItem(item);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
        return Response.success();
    }

    @GetMapping
    public Response<PageResponse<ItemDetailDTO>> listItems(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemQueryDTO queryDTO = new ItemQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setName(name);
        queryDTO.setPage(page);
        queryDTO.setSize(size);

        List<Item> items = itemService.queryItems(queryDTO);
        long total = itemService.countItems(queryDTO);

        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        Map<Long, List<Sku>> skusByItemId = itemIds.isEmpty() ? Map.of()
                : skuService.getSkusByItemIds(itemIds).stream()
                        .collect(Collectors.groupingBy(Sku::getItemId));

        Set<Long> categoryIds = items.stream()
                .map(Item::getCategoryId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Category> categoryMap = categoryIds.stream()
                .map(categoryService::getCategoryById)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(Category::getId, c -> c, (a, b) -> a));

        List<ItemDetailDTO> detailDTOs = items.stream().map(item -> {
            ItemDetailDTO dto = new ItemDetailDTO();
            dto.setItem(item);
            dto.setSkus(skusByItemId.getOrDefault(item.getId(), List.of()));

            if (item.getCategoryId() != null) {
                Category category = categoryMap.get(item.getCategoryId());
                if (category != null) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setId(category.getId());
                    categoryDTO.setName(category.getName());
                    dto.setCategory(categoryDTO);
                }
            }

            return dto;
        }).collect(Collectors.toList());

        PageResponse<ItemDetailDTO> pageResponse = PageResponse.of(detailDTOs, total, page, size);
        return Response.success(pageResponse);
    }

    @PostMapping("/{itemId}/skus")
    public Response<Long> addSkuToItem(@PathVariable("itemId") Long itemId, @RequestBody Sku sku) {
        itemService.addSkuToItem(itemId, sku);
        return Response.success(sku.getId());
    }

    @GetMapping("/{itemId}/skus")
    public Response<List<Sku>> getItemSkus(@PathVariable("itemId") Long itemId) {
        List<Sku> skus = itemService.getSkusByItemId(itemId);
        return Response.success(skus);
    }

    @PutMapping("/{itemId}/skus/{skuId}")
    public Response<Void> updateItemSku(@PathVariable("itemId") Long itemId,
                                        @PathVariable("skuId") Long skuId,
                                        @RequestBody Sku sku) {
        sku.setId(skuId);
        sku.setItemId(itemId);
        itemService.updateItemSku(sku);
        return Response.success();
    }

    @DeleteMapping("/{itemId}/skus/{skuId}")
    public Response<Void> deleteItemSku(@PathVariable("itemId") Long itemId,
                                        @PathVariable("skuId") Long skuId) {
        itemService.deleteItemSku(skuId);
        return Response.success();
    }
}
