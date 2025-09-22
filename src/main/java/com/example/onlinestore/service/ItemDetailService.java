package com.example.onlinestore.service;

import com.example.onlinestore.bean.ItemDetail;
import jakarta.validation.constraints.NotNull;

public interface ItemDetailService {
    /**
     * 获取商品详情
     * @param itemId 商品ID
     * @return 商品详情，包含商品信息和SKU列表
     */
    ItemDetail getItemDetail(@NotNull Long itemId);
} 