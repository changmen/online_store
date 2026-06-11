package com.example.onlinestore.service;

import com.example.onlinestore.bean.ItemDetail;
import jakarta.validation.constraints.NotNull;

public interface ItemDetailService {
    ItemDetail getItemDetail(@NotNull Long itemId);
} 