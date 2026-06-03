package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.dto.converter.ItemResponseConverter;
import com.example.onlinestore.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public Response<Page<ItemResponse>> listItems(@Valid ItemListQueryRequest queryRequest) {
        Page<Item> itemPage = itemService.listItems(queryRequest);
        Page<ItemResponse> responsePage = Page.of(
                itemPage.getItems().stream().map(itemResponseConverter::convert).toList(),
                itemPage.getTotalCount(),
                itemPage.getPageNum(),
                itemPage.getPageSize()
        );
        return Response.success(responsePage);
    }

    @PostMapping("")
    public Response<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemService.createItem(request);
        return Response.success(itemResponseConverter.convert(item));
    }

    @PutMapping("/{itemId}")
    public Response<Void> updateItem(@PathVariable("itemId") Long id,
                                     @Valid @RequestBody UpdateItemRequest request) {
        itemService.updateItem(id, request);
        return Response.success();
    }

}
