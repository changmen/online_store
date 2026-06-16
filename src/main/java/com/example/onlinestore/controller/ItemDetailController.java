package com.example.onlinestore.controller;

import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.dto.ItemDetailResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.converter.ItemDetailConverter;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.service.ItemDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@Validated
@RequiredArgsConstructor
public class ItemDetailController {
    private final ItemDetailService itemDetailService;
    private final ItemDetailConverter itemDetailConverter;
    private final ItemAccessLogService itemAccessLogService;

    @GetMapping("/{itemId}/detail")
    public Response<ItemDetailResponse> getItemDetail(@Positive @PathVariable("itemId") Long itemId, HttpServletRequest request) {
        ItemDetail detail = itemDetailService.getItemDetail(itemId);
        itemAccessLogService.recordItemDetailAccess(itemId, detail.getItem().getName(), request);
        return Response.success(itemDetailConverter.convert(detail));
    }
} 