package com.example.onlinestore.service;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.ItemQueryDTO;

import java.util.List;

public interface ItemService {
    void addItem(Item item);
    Item getItemById(long itemId);
    void updateItem(Item item);
    void deleteItem(long itemId);
    List<Item> getAllItems(int page, int size);
    
    /**
     * 按条件查询商品（支持类目ID精确搜索和商品名称模糊搜索）
     */
    List<Item> queryItems(ItemQueryDTO queryDTO);
    
    /**
     * 按条件统计商品总数
     */
    long countItems(ItemQueryDTO queryDTO);
    
    /**
     * 为商品添加SKU
     */
    void addSkuToItem(Long itemId, Sku sku);
    
    /**
     * 获取商品的所有SKU
     */
    List<Sku> getSkusByItemId(Long itemId);
    
    /**
     * 更新商品的SKU
     */
    void updateItemSku(Sku sku);
    
    /**
     * 删除商品的SKU
     */
    void deleteItemSku(Long skuId);
}
