package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import com.example.onlinestore.service.ItemService;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public Item createItem(CreateItemRequest request) {
        // 校验
        return null;
    }

    @Override
    public void updateItem(Long id, CreateItemRequest request) {

    }
}
