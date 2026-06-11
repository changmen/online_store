package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.dto.ItemDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDetailConverter {

    private final ItemResponseConverter itemResponseConverter;
    private final SkuConverter skuConverter;

    public ItemDetailResponse convert(ItemDetail itemDetail) {
        if (itemDetail == null) {
            return null;
        }
        ItemDetailResponse response = new ItemDetailResponse();
        response.setItem(itemResponseConverter.convert(itemDetail.getItem()));
        response.setSkus(itemDetail.getSkus() != null
                ? itemDetail.getSkus().stream().map(skuConverter::convert).collect(Collectors.toList())
                : Collections.emptyList());
        return response;
    }
}
