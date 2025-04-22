package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.ItemResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.converter.ItemResponseConverter;
import com.example.onlinestore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemResponseConverter itemResponseConverter;

    @GetMapping("/{itemId}")
    public Response<ItemResponse> getItemById(@PathVariable("itemId") Long id) {
        Item item = itemService.getItemById(id);
        return Response.success(itemResponseConverter.convert(item));
    }

}
