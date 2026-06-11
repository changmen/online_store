package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.dto.converter.ItemResponseConverter;
import com.example.onlinestore.service.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemResponseConverter itemResponseConverter;

    @GetMapping("/{itemId}")
    public Response<ItemResponse> getItemById(@Positive @PathVariable("itemId") Long id) {
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
    @PreAuthorize("hasRole('ADMIN')")
    public Response<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemService.createItem(request);
        return Response.success(itemResponseConverter.convert(item));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Void> updateItem(@Positive @PathVariable("itemId") Long id,
                                     @Valid @RequestBody UpdateItemRequest request) {
        itemService.updateItem(id, request);
        return Response.success();
    }

}
